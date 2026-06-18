package com.pricingia.saas.support.integration;

import com.pricingia.saas.support.container.PostgresTestContainer;
import com.pricingia.saas.support.container.RabbitMqTestContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Base for web-layer integration tests.
 *
 * <p>Boots the real application on a random port against real PostgreSQL and RabbitMQ
 * (Testcontainers) and exposes a tiny HTTP client. The context is cached and shared by
 * all subclasses, so startup happens once.</p>
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WebIntegrationTest {

	protected static final String WEBHOOK_SECRET = "integration-test-secret";

	private static final HttpClient HTTP = HttpClient.newHttpClient();

	static {
		PostgresTestContainer.getInstance().start();
		RabbitMqTestContainer.getInstance().start();
	}

	@Value("${local.server.port}")
	protected int port;

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
		registry.add("spring.rabbitmq.listener.simple.default-requeue-rejected", () -> "false");

		registry.add("management.endpoints.web.exposure.include", () -> "health,info");
		registry.add("pricingia.shopify.webhook.secret", () -> WEBHOOK_SECRET);
		registry.add("pricingia.shopify.webhooks.exchange", () -> "shopify.webhooks.exchange");
		registry.add("pricingia.shopify.webhooks.queue", () -> "shopify.webhooks.received.queue");
		registry.add("pricingia.shopify.webhooks.routing-key", () -> "shopify.webhook.received");
	}

	protected String url(String path) {
		return "http://localhost:" + port + path;
	}

	protected HttpResponse<String> get(String path) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url(path)))
				.GET()
				.build();
		return HTTP.send(request, HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> putJson(String path, String json) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url(path)))
				.header("Content-Type", "application/json")
				.PUT(HttpRequest.BodyPublishers.ofString(json))
				.build();
		return HTTP.send(request, HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> postJson(String path, String json) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url(path)))
				.header("Content-Type", "application/json")
				.POST(json == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json))
				.build();
		return HTTP.send(request, HttpResponse.BodyHandlers.ofString());
	}

	protected HttpResponse<String> postWithHeaders(String path, String body, Map<String, String> headers) throws Exception {
		HttpRequest.Builder builder = HttpRequest.newBuilder()
				.uri(URI.create(url(path)))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body));
		headers.forEach(builder::header);
		return HTTP.send(builder.build(), HttpResponse.BodyHandlers.ofString());
	}
}
