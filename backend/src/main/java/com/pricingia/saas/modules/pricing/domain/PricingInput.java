package com.pricingia.saas.modules.pricing.domain;

import java.math.BigDecimal;

/**
 * Pure inputs required to evaluate a pricing decision. No Spring / JPA / Shopify here.
 */
public record PricingInput(
		BigDecimal currentPrice,
		BigDecimal cost,
		BigDecimal minMarginPercentage,
		BigDecimal maxPriceIncreasePercentage,
		boolean automationEnabled
) {
}
