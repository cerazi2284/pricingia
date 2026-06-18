package com.pricingia.saas.modules.merchant.integration;

import com.pricingia.saas.support.integration.WebIntegrationTest;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class MerchantApiIT extends WebIntegrationTest {

	@Test
	void shouldReturnDemoSettings() throws Exception {
		HttpResponse<String> response = get("/api/merchant/settings");

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("demo-shop.myshopify.com");
		assertThat(response.body()).contains("\"currency\":\"USD\"");
	}

	@Test
	void shouldUpdateSettings() throws Exception {
		String payload = """
				{
				  "currency": "USD",
				  "minMarginPercentage": 35.00,
				  "maxPriceIncreasePercentage": 20.00,
				  "automationEnabled": true
				}
				""";

		HttpResponse<String> response = putJson("/api/merchant/settings", payload);

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(response.body()).contains("\"minMarginPercentage\":35");
		assertThat(response.body()).contains("\"automationEnabled\":true");
	}
}
