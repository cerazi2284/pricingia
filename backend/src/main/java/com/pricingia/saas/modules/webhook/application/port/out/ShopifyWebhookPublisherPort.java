package com.pricingia.saas.modules.webhook.application.port.out;

import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookEvent;

public interface ShopifyWebhookPublisherPort {

	void publish(Long eventId, ShopifyWebhookEvent event);
}
