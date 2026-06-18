package com.pricingia.saas.modules.shopify.domain;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * Validates Shopify webhook signatures.
 *
 * <p>Shopify signs the raw request body with HMAC-SHA256 using the shared secret and
 * sends the Base64-encoded digest in the {@code X-Shopify-Hmac-Sha256} header.</p>
 *
 * <p>Pure domain utility: no Spring, no HTTP, no persistence.</p>
 */
public final class ShopifyHmacVerifier {

	private ShopifyHmacVerifier() {
	}

	public static boolean isValid(String sharedSecret, String rawBody, String receivedHmacBase64) {
		requireNonNull(sharedSecret, "sharedSecret");
		requireNonNull(rawBody, "rawBody");
		requireNonNull(receivedHmacBase64, "receivedHmacBase64");

		String computed = computeBase64(sharedSecret, rawBody);
		return constantTimeEquals(
				computed.getBytes(StandardCharsets.US_ASCII),
				receivedHmacBase64.trim().getBytes(StandardCharsets.US_ASCII)
		);
	}

	public static String computeBase64(String sharedSecret, String rawBody) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(sharedSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] digest = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(digest);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to compute HMAC", e);
		}
	}

	private static boolean constantTimeEquals(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		int diff = 0;
		for (int i = 0; i < a.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}
}
