package com.pricingia.saas.common.exception;

import java.time.OffsetDateTime;

/**
 * Standardized error payload returned by the API.
 */
public record ApiError(
		OffsetDateTime timestamp,
		int status,
		String error,
		String message,
		String path
) {
}
