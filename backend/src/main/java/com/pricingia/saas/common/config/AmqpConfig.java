package com.pricingia.saas.common.config;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AMQP message conversion.
 *
 * <p>Uses the default (Java serialization) converter but explicitly allow-lists our own
 * classes, since Spring AMQP blocks deserialization of unknown classes by default.
 * Spring Boot wires this bean into both the RabbitTemplate and the listener container
 * factory, so producer and consumer stay symmetric.</p>
 */
@Configuration
public class AmqpConfig {

	@Bean
	MessageConverter rabbitMessageConverter() {
		SimpleMessageConverter converter = new SimpleMessageConverter();
		converter.addAllowedListPatterns("com.pricingia.saas.*", "java.*");
		return converter;
	}
}
