package com.pricingia.saas.modules.webhook.infrastructure.messaging;

import com.pricingia.saas.modules.webhook.application.port.out.ShopifyWebhookPublisherPort;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqShopifyWebhookPublisher implements ShopifyWebhookPublisherPort {

	private final RabbitTemplate rabbitTemplate;
	private final ShopifyWebhookMessagingProperties props;

	public RabbitMqShopifyWebhookPublisher(RabbitTemplate rabbitTemplate, ShopifyWebhookMessagingProperties props) {
		this.rabbitTemplate = rabbitTemplate;
		this.props = props;
	}

	@Override
	public void publish(Long eventId, ShopifyWebhookEvent event) {
		ShopifyWebhookMessage message = new ShopifyWebhookMessage(
				eventId,
				event.webhookId(),
				event.topic(),
				event.shopDomain(),
				event.payload(),
				event.receivedAt()
		);

		rabbitTemplate.convertAndSend(props.exchange(), props.routingKey(), message);
	}
}
