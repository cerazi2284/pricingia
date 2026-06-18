package com.pricingia.saas.modules.webhook.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopifyWebhookEventRepository extends JpaRepository<ShopifyWebhookEventEntity, Long> {

	Optional<ShopifyWebhookEventEntity> findByWebhookId(String webhookId);

	boolean existsByWebhookId(String webhookId);
}
