package com.pricingia.saas.modules.webhook.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pricingia.shopify.webhooks")
public record ShopifyWebhookMessagingProperties(
		String exchange,
		String queue,
		String routingKey
) {
}

