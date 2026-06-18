package com.pricingia.saas.modules.pricing.presentation.dto;

import com.pricingia.saas.modules.pricing.domain.PriceDecisionStatus;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionEntity;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceDecisionResponse(
		Long id,
		Long merchantSettingsId,
		Long monitoredProductId,
		String productTitle,
		String decisionType,
		BigDecimal oldPrice,
		BigDecimal suggestedPrice,
		String reason,
		PriceDecisionStatus status,
		Long sourceEventId,
		Instant createdAt,
		Instant updatedAt
) {
	public static PriceDecisionResponse from(PriceDecisionEntity entity, String productTitle, String decisionType) {
		return new PriceDecisionResponse(
				entity.getId(),
				entity.getMerchantSettingsId(),
				entity.getMonitoredProductId(),
				productTitle,
				decisionType,
				entity.getOldPrice(),
				entity.getSuggestedPrice(),
				entity.getReason(),
				entity.getStatus(),
				entity.getSourceEventId(),
				entity.getCreatedAt(),
				entity.getUpdatedAt()
		);
	}
}
