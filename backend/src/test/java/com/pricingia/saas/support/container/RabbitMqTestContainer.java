package com.pricingia.saas.support.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public final class RabbitMqTestContainer {

	private static final DockerImageName IMAGE = DockerImageName.parse("rabbitmq:3-management-alpine");

	private static final int AMQP_PORT = 5672;
	private static final int MANAGEMENT_PORT = 15672;

	private static final GenericContainer<?> INSTANCE =
			new GenericContainer<>(IMAGE)
					.withEnv("RABBITMQ_DEFAULT_USER", "pricingia_user")
					.withEnv("RABBITMQ_DEFAULT_PASS", "pricingia_pass")
					.withExposedPorts(AMQP_PORT, MANAGEMENT_PORT);

	private RabbitMqTestContainer() {
	}

	public static GenericContainer<?> getInstance() {
		return INSTANCE;
	}

	public static int getAmqpPort() {
		return getInstance().getMappedPort(AMQP_PORT);
	}
}

