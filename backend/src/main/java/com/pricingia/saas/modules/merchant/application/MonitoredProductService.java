package com.pricingia.saas.modules.merchant.application;

import com.pricingia.saas.common.exception.NotFoundException;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class MonitoredProductService {

	private static final Logger log = LoggerFactory.getLogger(MonitoredProductService.class);

	private final MonitoredProductRepository repository;
	private final MerchantSettingsService merchantSettingsService;

	public MonitoredProductService(MonitoredProductRepository repository, MerchantSettingsService merchantSettingsService) {
		this.repository = repository;
		this.merchantSettingsService = merchantSettingsService;
	}

	@Transactional(readOnly = true)
	public List<MonitoredProductEntity> listForCurrentMerchant() {
		MerchantSettingsEntity merchant = merchantSettingsService.getCurrent();
		return repository.findByMerchantSettingsId(merchant.getId());
	}

	@Transactional(readOnly = true)
	public MonitoredProductEntity getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Monitored product not found: " + id));
	}

	/**
	 * Temporary endpoint support: seeds demo products with a margin below the minimum so the
	 * pricing engine produces a SUGGESTED decision. To be removed once real Shopify sync exists.
	 */
	@Transactional
	public List<MonitoredProductEntity> createDemoProducts() {
		MerchantSettingsEntity merchant = merchantSettingsService.getCurrent();
		Instant now = Instant.now();

		MonitoredProductEntity productA = new MonitoredProductEntity(
				merchant.getId(), "gid://shopify/Product/1001", "Demo Hoodie", "DEMO-HOODIE-001",
				new BigDecimal("50.0000"), new BigDecimal("40.0000"), 25, true, now);

		MonitoredProductEntity productB = new MonitoredProductEntity(
				merchant.getId(), "gid://shopify/Product/1002", "Demo Sneakers", "DEMO-SNEAKERS-002",
				new BigDecimal("120.0000"), new BigDecimal("70.0000"), 12, true, now);

		List<MonitoredProductEntity> saved = repository.saveAll(List.of(productA, productB));
		log.info("Seeded {} demo monitored products for shop={}", saved.size(), merchant.getShopDomain());
		return saved;
	}
}
