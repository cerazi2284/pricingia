package com.pricingia.saas.modules.pricing.infrastructure.messaging;

import com.pricingia.saas.modules.pricing.application.ProcessShopifyWebhookEventService;
import com.pricingia.saas.modules.webhook.infrastructure.messaging.ShopifyWebhookMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes received Shopify webhook events and delegates to the pricing engine.
 */
@Component
public class ShopifyWebhookEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(ShopifyWebhookEventConsumer.class);

	private final ProcessShopifyWebhookEventService service;

	public ShopifyWebhookEventConsumer(ProcessShopifyWebhookEventService service) {
		this.service = service;
	}

	@RabbitListener(queues = "${pricingia.shopify.webhooks.queue}")
	public void onMessage(ShopifyWebhookMessage message) {
		log.info("Consuming webhook event: eventId={} topic={}", message.eventId(), message.topic());
		service.handle(message.eventId(), message.payload());
	}
}
