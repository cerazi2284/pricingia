package com.pricingia.saas.modules.merchant.presentation.dto;

import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductEntity;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
		Long id,
		Long merchantSettingsId,
		String shopifyProductId,
		String title,
		String sku,
		BigDecimal currentPrice,
		BigDecimal cost,
		int inventoryQuantity,
		boolean active,
		Instant createdAt,
		Instant updatedAt
) {
	public static ProductResponse from(MonitoredProductEntity entity) {
		return new ProductResponse(
				entity.getId(),
				entity.getMerchantSettingsId(),
				entity.getShopifyProductId(),
				entity.getTitle(),
				entity.getSku(),
				entity.getCurrentPrice(),
				entity.getCost(),
				entity.getInventoryQuantity(),
				entity.isActive(),
				entity.getCreatedAt(),
				entity.getUpdatedAt()
		);
	}
}
