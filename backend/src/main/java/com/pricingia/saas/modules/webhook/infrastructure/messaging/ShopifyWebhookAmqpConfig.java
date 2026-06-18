package com.pricingia.saas.modules.webhook.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShopifyWebhookMessagingProperties.class)
public class ShopifyWebhookAmqpConfig {

	@Bean
	DirectExchange shopifyWebhooksExchange(ShopifyWebhookMessagingProperties props) {
		return new DirectExchange(props.exchange(), true, false);
	}

	@Bean
	Queue shopifyWebhooksQueue(ShopifyWebhookMessagingProperties props) {
		return new Queue(props.queue(), true);
	}

	@Bean
	Binding shopifyWebhooksBinding(DirectExchange shopifyWebhooksExchange, Queue shopifyWebhooksQueue, ShopifyWebhookMessagingProperties props) {
		return BindingBuilder.bind(shopifyWebhooksQueue).to(shopifyWebhooksExchange).with(props.routingKey());
	}

	@Bean
	RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
		// Ensures exchange/queue/binding are declared on startup
		return new RabbitAdmin(rabbitTemplate);
	}
}

