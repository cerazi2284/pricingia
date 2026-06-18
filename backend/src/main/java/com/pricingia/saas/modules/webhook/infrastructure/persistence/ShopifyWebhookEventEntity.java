package com.pricingia.saas.modules.webhook.infrastructure.persistence;

import com.pricingia.saas.modules.webhook.domain.WebhookStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
		name = "shopify_webhook_events",
		schema = "pricingia",
		uniqueConstraints = @UniqueConstraint(name = "ux_shopify_webhook_events_webhook_id", columnNames = "webhook_id")
)
public class ShopifyWebhookEventEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "webhook_id", nullable = false, length = 128)
	private String webhookId;

	@Column(name = "topic", nullable = false, length = 255)
	private String topic;

	@Column(name = "shop_domain", nullable = false, length = 255)
	private String shopDomain;

	@Column(name = "payload", nullable = false, columnDefinition = "TEXT")
	private String payload;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private WebhookStatus status;

	@Column(name = "received_at", nullable = false)
	private Instant receivedAt;

	@Column(name = "processed_at")
	private Instant processedAt;

	protected ShopifyWebhookEventEntity() {
	}

	public ShopifyWebhookEventEntity(String webhookId, String topic, String shopDomain, String payload,
			WebhookStatus status, Instant receivedAt) {
		this.webhookId = webhookId;
		this.topic = topic;
		this.shopDomain = shopDomain;
		this.payload = payload;
		this.status = status;
		this.receivedAt = receivedAt;
	}

	public Long getId() {
		return id;
	}

	public String getWebhookId() {
		return webhookId;
	}

	public String getTopic() {
		return topic;
	}

	public String getShopDomain() {
		return shopDomain;
	}

	public String getPayload() {
		return payload;
	}

	public WebhookStatus getStatus() {
		return status;
	}

	public void setStatus(WebhookStatus status) {
		this.status = status;
	}

	public Instant getReceivedAt() {
		return receivedAt;
	}

	public Instant getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(Instant processedAt) {
		this.processedAt = processedAt;
	}
}
