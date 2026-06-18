package com.pricingia.saas.modules.webhook.application.usecase;

import com.pricingia.saas.modules.shopify.domain.ShopifyHmacVerifier;
import com.pricingia.saas.modules.webhook.application.port.out.ShopifyWebhookPublisherPort;
import com.pricingia.saas.modules.webhook.application.port.out.WebhookIdempotencyPort;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookEvent;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookHeaders;
import com.pricingia.saas.modules.webhook.domain.WebhookPublishUnavailableException;
import com.pricingia.saas.modules.webhook.domain.WebhookUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

/**
 * Validates, deduplicates, persists and publishes an incoming Shopify webhook.
 */
@Service
public class ProcessShopifyWebhookUseCase {

	private static final Logger log = LoggerFactory.getLogger(ProcessShopifyWebhookUseCase.class);

	private final WebhookIdempotencyPort idempotencyPort;
	private final ShopifyWebhookPublisherPort publisherPort;
	private final String sharedSecret;

	public ProcessShopifyWebhookUseCase(
			WebhookIdempotencyPort idempotencyPort,
			ShopifyWebhookPublisherPort publisherPort,
			@Value("${pricingia.shopify.webhook.secret:}") String sharedSecret
	) {
		this.idempotencyPort = idempotencyPort;
		this.publisherPort = publisherPort;
		this.sharedSecret = sharedSecret;
	}

	public ProcessShopifyWebhookResult process(ShopifyWebhookHeaders headers, String rawBody) {
		if (sharedSecret == null || sharedSecret.isBlank()) {
			throw new WebhookPublishUnavailableException(
					new IllegalStateException("Missing pricingia.shopify.webhook.secret"));
		}

		if (!ShopifyHmacVerifier.isValid(sharedSecret, rawBody, headers.hmacSha256())) {
			throw new WebhookUnauthorizedException();
		}

		ShopifyWebhookEvent event = new ShopifyWebhookEvent(
				headers.webhookId(),
				headers.topic(),
				headers.shopDomain(),
				rawBody,
				Instant.now()
		);

		Optional<Long> reserved = idempotencyPort.reserveIfNew(event);
		if (reserved.isEmpty()) {
			log.info("Duplicate webhook ignored: webhookId={} topic={}", headers.webhookId(), headers.topic());
			return ProcessShopifyWebhookResult.DUPLICATE;
		}

		Long eventId = reserved.get();
		try {
			publisherPort.publish(eventId, event);
			idempotencyPort.markPublished(eventId);
			log.info("Webhook accepted and published: webhookId={} topic={} eventId={}",
					headers.webhookId(), headers.topic(), eventId);
			return ProcessShopifyWebhookResult.ACCEPTED_AND_PUBLISHED;
		} catch (Exception ex) {
			idempotencyPort.markFailed(eventId);
			throw new WebhookPublishUnavailableException(ex);
		}
	}
}
