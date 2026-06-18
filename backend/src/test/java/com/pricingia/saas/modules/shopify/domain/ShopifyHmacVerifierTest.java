package com.pricingia.saas.modules.shopify.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShopifyHmacVerifierTest {

	private static final String SECRET = "unit-test-secret";
	private static final String RAW_BODY = "{\"hello\":\"world\"}";

	@Test
	void shouldValidateHmacComputedFromRawBody() {
		String hmac = ShopifyHmacVerifier.computeBase64(SECRET, RAW_BODY);

		assertThat(ShopifyHmacVerifier.isValid(SECRET, RAW_BODY, hmac)).isTrue();
	}

	@Test
	void shouldRejectHmacWhenBodyDiffers() {
		String hmac = ShopifyHmacVerifier.computeBase64(SECRET, RAW_BODY);

		assertThat(ShopifyHmacVerifier.isValid(SECRET, RAW_BODY + " ", hmac)).isFalse();
	}

	@Test
	void shouldRejectHmacWhenSecretDiffers() {
		String hmac = ShopifyHmacVerifier.computeBase64(SECRET, RAW_BODY);

		assertThat(ShopifyHmacVerifier.isValid("other-secret", RAW_BODY, hmac)).isFalse();
	}
}
