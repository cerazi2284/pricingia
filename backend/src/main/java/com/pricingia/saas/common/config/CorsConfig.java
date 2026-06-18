package com.pricingia.saas.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS for browser clients (web dashboard). Patterns are configurable via
 * {@code pricingia.cors.allowed-origin-patterns} (comma-separated).
 */
@Configuration
public class CorsConfig {

	@Value("${pricingia.cors.allowed-origin-patterns:http://localhost:*,http://127.0.0.1:*}")
	private String allowedOriginPatterns;

	@Bean
	CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		Arrays.stream(allowedOriginPatterns.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.forEach(config::addAllowedOriginPattern);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
