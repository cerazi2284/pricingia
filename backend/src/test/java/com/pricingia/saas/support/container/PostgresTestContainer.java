package com.pricingia.saas.support.container;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public final class PostgresTestContainer {

	private static final DockerImageName IMAGE = DockerImageName.parse("postgres:16-alpine");

	private static final PostgreSQLContainer<?> INSTANCE =
			new PostgreSQLContainer<>(IMAGE)
					.withDatabaseName("pricingia_db")
					.withUsername("pricingia_user")
					.withPassword("pricingia_pass");

	private PostgresTestContainer() {
	}

	public static PostgreSQLContainer<?> getInstance() {
		return INSTANCE;
	}
}

