package com.pricingia.saas.common.exception;

/**
 * Maps to HTTP 503 (downstream dependency unavailable, e.g. message broker).
 */
public class ServiceUnavailableException extends RuntimeException {

	public ServiceUnavailableException(String message) {
		super(message);
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
