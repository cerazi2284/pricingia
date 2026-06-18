package com.pricingia.saas.modules.merchant.integration;

import com.pricingia.saas.support.integration.WebIntegrationTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ProductsApiIT extends WebIntegrationTest {

	@Test
	void shouldCreateDemoProductsAndListThem() throws Exception {
		HttpResponse<String> created = postJson("/api/products/demo", null);
		assertThat(created.statusCode()).isEqualTo(201);
		assertThat(created.body()).contains("Demo Hoodie");

		HttpResponse<String> list = get("/api/products");
		assertThat(list.statusCode()).isEqualTo(200);
		assertThat(countOccurrences(list.body(), "shopifyProductId")).isGreaterThanOrEqualTo(2);
	}

	private static int countOccurrences(String haystack, String needle) {
		int count = 0;
		int index = 0;
		while ((index = haystack.indexOf(needle, index)) != -1) {
			count++;
			index += needle.length();
		}
		return count;
	}
}
