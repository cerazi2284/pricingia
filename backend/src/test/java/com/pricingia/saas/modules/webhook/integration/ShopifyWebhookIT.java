package com.pricingia.saas.modules.webhook.integration;

import com.pricingia.saas.modules.shopify.domain.ShopifyHmacVerifier;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookHeaders;
import com.pricingia.saas.support.integration.WebIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ShopifyWebhookIT extends WebIntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void getShouldReturnWebhookInfoForBrowserDev() throws Exception {
		HttpResponse<String> response = get("/webhooks/shopify");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("POST");
		assertThat(response.body()).contains("X-Shopify-Hmac-Sha256");
	}

	@Test
	void validWebhookShouldReturn200AndPersistEvent() throws Exception {
		String body = "{\"event\":\"order_created\",\"id\":123}";
		String webhookId = "it-valid-001";
		String hmac = ShopifyHmacVerifier.computeBase64(WEBHOOK_SECRET, body);

		HttpResponse<String> response = postWebhook(body, hmac, webhookId, "orders/create");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(countEvents(webhookId)).isEqualTo(1);
	}

	@Test
	void invalidHmacShouldReturn401AndNotPersist() throws Exception {
		String body = "{\"event\":\"order_created\",\"id\":123}";
		String webhookId = "it-invalid-002";

		HttpResponse<String> response = postWebhook(body, "invalid-signature", webhookId, "orders/create");

		assertThat(response.statusCode()).isEqualTo(401);
		assertThat(countEvents(webhookId)).isZero();
	}

	@Test
	void missingRequiredHeaderShouldReturn400() throws Exception {
		String body = "{\"event\":\"order_created\"}";
		String hmac = ShopifyHmacVerifier.computeBase64(WEBHOOK_SECRET, body);

		Map<String, String> headers = new HashMap<>();
		headers.put(ShopifyWebhookHeaders.HEADER_HMAC, hmac);
		headers.put(ShopifyWebhookHeaders.HEADER_WEBHOOK_ID, "it-missing-003");
		headers.put(ShopifyWebhookHeaders.HEADER_SHOP_DOMAIN, "demo-shop.myshopify.com");
		// X-Shopify-Topic intentionally omitted

		HttpResponse<String> response = postWithHeaders("/webhooks/shopify", body, headers);

		assertThat(response.statusCode()).isEqualTo(400);
	}

	@Test
	void duplicateWebhookShouldReturn200AndPersistOnce() throws Exception {
		String body = "{\"event\":\"order_updated\",\"id\":456}";
		String webhookId = "it-duplicate-004";
		String hmac = ShopifyHmacVerifier.computeBase64(WEBHOOK_SECRET, body);

		assertThat(postWebhook(body, hmac, webhookId, "orders/updated").statusCode()).isEqualTo(200);
		assertThat(postWebhook(body, hmac, webhookId, "orders/updated").statusCode()).isEqualTo(200);

		assertThat(countEvents(webhookId)).isEqualTo(1);
	}

	private HttpResponse<String> postWebhook(String body, String hmac, String webhookId, String topic) throws Exception {
		Map<String, String> headers = new HashMap<>();
		headers.put(ShopifyWebhookHeaders.HEADER_HMAC, hmac);
		headers.put(ShopifyWebhookHeaders.HEADER_WEBHOOK_ID, webhookId);
		headers.put(ShopifyWebhookHeaders.HEADER_TOPIC, topic);
		headers.put(ShopifyWebhookHeaders.HEADER_SHOP_DOMAIN, "demo-shop.myshopify.com");
		return postWithHeaders("/webhooks/shopify", body, headers);
	}

	private int countEvents(String webhookId) {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from pricingia.shopify_webhook_events where webhook_id = ?",
				Integer.class, webhookId);
		return count == null ? 0 : count;
	}
}
