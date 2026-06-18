package com.pricingia.saas.modules.webhook.infrastructure.messaging;

import java.io.Serializable;
import java.time.Instant;

/**
 * Published contract for a received Shopify webhook. Consumed by the pricing module.
 *
 * <p>Serializable so it travels over RabbitMQ with the default message converter.</p>
 */
public record ShopifyWebhookMessage(
		Long eventId,
		String webhookId,
		String topic,
		String shopDomain,
		String payload,
		Instant receivedAt
) implements Serializable {
}
