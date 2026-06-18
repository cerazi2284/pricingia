package com.pricingia.saas.modules.webhook.presentation.controller;

import com.pricingia.saas.modules.webhook.application.usecase.ProcessShopifyWebhookUseCase;
import com.pricingia.saas.modules.webhook.domain.ShopifyWebhookHeaders;
import com.pricingia.saas.modules.webhook.domain.WebhookBadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thin controller: HTTP wiring only. Validation, dedup, persistence and publishing
 * happen in {@link ProcessShopifyWebhookUseCase}. Error-to-status mapping is handled
 * by the global exception handler (401/400/503).
 */
@RestController
public class ShopifyWebhookController {

	private final ProcessShopifyWebhookUseCase useCase;

	public ShopifyWebhookController(ProcessShopifyWebhookUseCase useCase) {
		this.useCase = useCase;
	}

	/**
	 * Dev helper: opening this URL in the browser shows how to call the real webhook (POST).
	 * Shopify only sends POST; this GET is not part of the Shopify contract.
	 */
	@GetMapping("/webhooks/shopify")
	public ResponseEntity<Map<String, Object>> webhookInfo() {
		return ResponseEntity.ok(Map.of(
				"endpoint", "/webhooks/shopify",
				"method", "POST",
				"description", "Shopify webhook receiver. Authenticated via HMAC on the raw body.",
				"requiredHeaders", List.of(
						ShopifyWebhookHeaders.HEADER_HMAC,
						ShopifyWebhookHeaders.HEADER_WEBHOOK_ID,
						ShopifyWebhookHeaders.HEADER_TOPIC,
						ShopifyWebhookHeaders.HEADER_SHOP_DOMAIN
				)
		));
	}

	@PostMapping(value = "/webhooks/shopify", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<Void> receive(@RequestBody(required = false) byte[] body, HttpServletRequest request) {
		if (body == null) {
			throw new WebhookBadRequestException("Missing request body");
		}

		ShopifyWebhookHeaders headers = ShopifyWebhookHeaders.from(toSingleValueHeaders(request));
		String rawBody = new String(body, StandardCharsets.UTF_8);

		useCase.process(headers, rawBody);
		return ResponseEntity.ok().build();
	}

	private static Map<String, String> toSingleValueHeaders(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();
		Enumeration<String> names = request.getHeaderNames();
		while (names != null && names.hasMoreElements()) {
			String name = names.nextElement();
			map.put(name, request.getHeader(name));
		}
		return map;
	}
}
