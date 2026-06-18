package com.pricingia.saas.modules.webhook.domain;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Required Shopify webhook headers. Missing/blank values map to HTTP 400.
 */
public record ShopifyWebhookHeaders(
		String hmacSha256,
		String webhookId,
		String topic,
		String shopDomain
) {
	public static final String HEADER_HMAC = "X-Shopify-Hmac-Sha256";
	public static final String HEADER_WEBHOOK_ID = "X-Shopify-Webhook-Id";
	public static final String HEADER_TOPIC = "X-Shopify-Topic";
	public static final String HEADER_SHOP_DOMAIN = "X-Shopify-Shop-Domain";

	public static ShopifyWebhookHeaders from(Map<String, String> headers) {
		requireNonNull(headers, "headers");

		String hmac = getRequired(headers, HEADER_HMAC);
		String id = getRequired(headers, HEADER_WEBHOOK_ID);
		String topic = getRequired(headers, HEADER_TOPIC);
		String shopDomain = getRequired(headers, HEADER_SHOP_DOMAIN);
		return new ShopifyWebhookHeaders(hmac, id, topic, shopDomain);
	}

	private static String getRequired(Map<String, String> headers, String name) {
		Optional<String> value = headers.entrySet().stream()
				.filter(e -> e.getKey() != null && e.getKey().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
				.map(Map.Entry::getValue)
				.filter(v -> v != null && !v.isBlank())
				.findFirst();

		if (value.isEmpty()) {
			throw new WebhookBadRequestException("Missing required header: " + name);
		}
		return value.get().trim();
	}
}
