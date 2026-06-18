package com.pricingia.saas.modules.analytics.presentation.dto;

import java.math.BigDecimal;

public record AnalyticsSummaryResponse(
		String shopDomain,
		String currency,
		long productsMonitored,
		long priceDecisions,
		long suggestions,
		long approved,
		long rejected,
		long webhookEventsReceived,
		BigDecimal potentialUplift
) {
}
