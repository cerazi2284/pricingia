package com.pricingia.saas.modules.analytics.application;

import com.pricingia.saas.modules.analytics.presentation.dto.AnalyticsSummaryResponse;
import com.pricingia.saas.modules.merchant.application.MerchantSettingsService;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductRepository;
import com.pricingia.saas.modules.pricing.domain.PriceDecisionStatus;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionEntity;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionRepository;
import com.pricingia.saas.modules.webhook.infrastructure.persistence.ShopifyWebhookEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AnalyticsService {

	private final MerchantSettingsService merchantSettingsService;
	private final MonitoredProductRepository productRepository;
	private final PriceDecisionRepository priceDecisionRepository;
	private final ShopifyWebhookEventRepository eventRepository;

	public AnalyticsService(
			MerchantSettingsService merchantSettingsService,
			MonitoredProductRepository productRepository,
			PriceDecisionRepository priceDecisionRepository,
			ShopifyWebhookEventRepository eventRepository) {
		this.merchantSettingsService = merchantSettingsService;
		this.productRepository = productRepository;
		this.priceDecisionRepository = priceDecisionRepository;
		this.eventRepository = eventRepository;
	}

	@Transactional(readOnly = true)
	public AnalyticsSummaryResponse summary() {
		MerchantSettingsEntity merchant = merchantSettingsService.getCurrent();

		long suggestions = priceDecisionRepository.countByStatus(PriceDecisionStatus.SUGGESTED);
		long approved = priceDecisionRepository.countByStatus(PriceDecisionStatus.APPROVED);
		long rejected = priceDecisionRepository.countByStatus(PriceDecisionStatus.REJECTED);

		BigDecimal potentialUplift = priceDecisionRepository.findAll().stream()
				.filter(d -> d.getStatus() == PriceDecisionStatus.SUGGESTED
						|| d.getStatus() == PriceDecisionStatus.APPROVED)
				.map(PriceDecisionEntity::computeUplift)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_UP);

		return new AnalyticsSummaryResponse(
				merchant.getShopDomain(),
				merchant.getCurrency(),
				productRepository.countByActiveTrue(),
				priceDecisionRepository.count(),
				suggestions,
				approved,
				rejected,
				eventRepository.count(),
				potentialUplift
		);
	}
}
