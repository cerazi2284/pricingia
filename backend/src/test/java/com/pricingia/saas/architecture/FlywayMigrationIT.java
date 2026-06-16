package com.pricingia.saas.architecture;

import com.pricingia.saas.support.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayMigrationIT extends IntegrationTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void shouldApplyInitialFlywayMigration() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from pricingia.flyway_schema_history where version = '1'",
				Integer.class
		);

		assertThat(count).isNotNull();
		assertThat(count).isGreaterThanOrEqualTo(1);
	}
}

