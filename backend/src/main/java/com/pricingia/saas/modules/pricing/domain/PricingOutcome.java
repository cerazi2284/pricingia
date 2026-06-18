package com.pricingia.saas.modules.pricing.domain;

import java.math.BigDecimal;

/**
 * Result of evaluating {@link PricingRule}. {@code suggestedPrice} equals the current
 * price when no change is recommended (status FAILED).
 */
public record PricingOutcome(
		PriceDecisionStatus status,
		BigDecimal oldPrice,
		BigDecimal suggestedPrice,
		String reason
) {
	public boolean isSuggested() {
		return status == PriceDecisionStatus.SUGGESTED;
	}
}
