package com.pricingia.saas.modules.webhook.infrastructure.persistence;

import com.pricingia.saas.modules.webhook.application.port.out.WebhookIdempotencyPort;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookEvent;
import com.pricingia.saas.modules.webhook.domain.WebhookStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class JpaWebhookIdempotencyAdapter implements WebhookIdempotencyPort {

	private final ShopifyWebhookEventRepository repository;

	public JpaWebhookIdempotencyAdapter(ShopifyWebhookEventRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Optional<Long> reserveIfNew(ShopifyWebhookEvent event) {
		if (repository.existsByWebhookId(event.webhookId())) {
			return Optional.empty();
		}

		ShopifyWebhookEventEntity entity = new ShopifyWebhookEventEntity(
				event.webhookId(),
				event.topic(),
				event.shopDomain(),
				event.payload(),
				WebhookStatus.RECEIVED,
				event.receivedAt()
		);
		return Optional.of(repository.save(entity).getId());
	}

	@Override
	@Transactional
	public void markPublished(Long eventId) {
		repository.findById(eventId).ifPresent(e -> e.setStatus(WebhookStatus.PUBLISHED));
	}

	@Override
	@Transactional
	public void markFailed(Long eventId) {
		repository.findById(eventId).ifPresent(e -> e.setStatus(WebhookStatus.FAILED));
	}
}
