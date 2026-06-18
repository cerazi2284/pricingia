package com.pricingia.saas.modules.webhook.application.port.out;

import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookEvent;

import java.util.Optional;

/**
 * Idempotency + persistence boundary for received Shopify webhooks.
 */
public interface WebhookIdempotencyPort {

	/**
	 * Persists the event with status RECEIVED if its webhook id was not seen before.
	 *
	 * @return the persisted event id when newly reserved, or empty when it is a duplicate.
	 */
	Optional<Long> reserveIfNew(ShopifyWebhookEvent event);

	void markPublished(Long eventId);

	void markFailed(Long eventId);
}
