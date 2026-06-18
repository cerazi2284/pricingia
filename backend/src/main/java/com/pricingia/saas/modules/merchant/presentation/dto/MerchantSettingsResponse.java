package com.pricingia.saas.modules.merchant.presentation.dto;

import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsEntity;

import java.math.BigDecimal;
import java.time.Instant;

public record MerchantSettingsResponse(
		Long id,
		String shopDomain,
		String currency,
		BigDecimal minMarginPercentage,
		BigDecimal maxPriceIncreasePercentage,
		boolean automationEnabled,
		Instant createdAt,
		Instant updatedAt
) {
	public static MerchantSettingsResponse from(MerchantSettingsEntity entity) {
		return new MerchantSettingsResponse(
				entity.getId(),
				entity.getShopDomain(),
				entity.getCurrency(),
				entity.getMinMarginPercentage(),
				entity.getMaxPriceIncreasePercentage(),
				entity.isAutomationEnabled(),
				entity.getCreatedAt(),
				entity.getUpdatedAt()
		);
	}
}
