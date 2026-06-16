package com.pricingia.saas.support.integration;

import com.pricingia.saas.support.container.PostgresTestContainer;
import com.pricingia.saas.support.container.RabbitMqTestContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base for integration tests.
 *
 * <p>Runs with real infrastructure via Testcontainers (PostgreSQL + RabbitMQ).</p>
 */
@Testcontainers
@SpringBootTest
public abstract class IntegrationTest {

	static {
		PostgresTestContainer.getInstance().start();
		RabbitMqTestContainer.getInstance().start();
	}

	@DynamicPropertySource
	static void registerDynamicProperties(DynamicPropertyRegistry registry) {
		var postgres = PostgresTestContainer.getInstance();

		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);

		registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
		registry.add("spring.jpa.open-in-view", () -> "false");
		registry.add("spring.jpa.properties.hibernate.default_schema", () -> "pricingia");

		registry.add("spring.flyway.enabled", () -> "true");
		registry.add("spring.flyway.schemas", () -> "pricingia");
		registry.add("spring.flyway.default-schema", () -> "pricingia");
		registry.add("spring.flyway.locations", () -> "classpath:db/migration");

		registry.add("spring.rabbitmq.host", () -> "localhost");
		registry.add("spring.rabbitmq.port", RabbitMqTestContainer::getAmqpPort);
		registry.add("spring.rabbitmq.username", () -> "pricingia_user");
		registry.add("spring.rabbitmq.password", () -> "pricingia_pass");

		registry.add("management.endpoints.web.exposure.include", () -> "health,info");
	}
}

