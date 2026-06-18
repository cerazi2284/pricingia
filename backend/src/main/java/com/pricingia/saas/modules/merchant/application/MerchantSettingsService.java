package com.pricingia.saas.modules.merchant.application;

import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Single-tenant MVP: there is one demo merchant. Settings are created on first read
 * if they do not exist yet. Real multi-tenant + auth comes later.
 */
@Service
public class MerchantSettingsService {

	private static final Logger log = LoggerFactory.getLogger(MerchantSettingsService.class);

	public static final String DEMO_SHOP_DOMAIN = "demo-shop.myshopify.com";
	public static final String DEMO_CURRENCY = "USD";
	private static final BigDecimal DEMO_MIN_MARGIN = new BigDecimal("30.00");
	private static final BigDecimal DEMO_MAX_INCREASE = new BigDecimal("15.00");

	private final MerchantSettingsRepository repository;

	public MerchantSettingsService(MerchantSettingsRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public MerchantSettingsEntity getCurrent() {
		return repository.findByShopDomain(DEMO_SHOP_DOMAIN)
				.orElseGet(this::createDemo);
	}

	@Transactional
	public MerchantSettingsEntity update(String currency, BigDecimal minMarginPercentage,
			BigDecimal maxPriceIncreasePercentage, boolean automationEnabled) {
		MerchantSettingsEntity entity = getCurrent();
		entity.setCurrency(currency);
		entity.setMinMarginPercentage(minMarginPercentage);
		entity.setMaxPriceIncreasePercentage(maxPriceIncreasePercentage);
		entity.setAutomationEnabled(automationEnabled);
		entity.setUpdatedAt(Instant.now());
		log.info("Updated merchant settings for shop={}", entity.getShopDomain());
		return repository.save(entity);
	}

	private MerchantSettingsEntity createDemo() {
		log.info("Seeding demo merchant settings for shop={}", DEMO_SHOP_DOMAIN);
		MerchantSettingsEntity entity = new MerchantSettingsEntity(
				DEMO_SHOP_DOMAIN, DEMO_CURRENCY, DEMO_MIN_MARGIN, DEMO_MAX_INCREASE, true, Instant.now());
		return repository.save(entity);
	}
}
