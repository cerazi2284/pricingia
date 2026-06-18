package com.pricingia.saas.modules.webhook.domain;

import java.time.Instant;

public record ShopifyWebhookEvent(
		String webhookId,
		String topic,
		String shopDomain,
		String payload,
		Instant receivedAt
) {
}
