package com.pricingia.saas.modules.webhook.integration;

import com.pricingia.saas.support.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class WebhookMigrationIT extends IntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldCreateCoreBusinessTablesAfterFlyway() {
		assertThat(tableExists("merchant_settings")).isTrue();
		assertThat(tableExists("monitored_products")).isTrue();
		assertThat(tableExists("shopify_webhook_events")).isTrue();
		assertThat(tableExists("price_decisions")).isTrue();
	}

	private boolean tableExists(String tableName) {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from information_schema.tables where table_schema = 'pricingia' and table_name = ?",
				Integer.class, tableName);
		return count != null && count == 1;
	}
}
