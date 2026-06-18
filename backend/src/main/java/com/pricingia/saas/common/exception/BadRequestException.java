package com.pricingia.saas.common.exception;

/**
 * Maps to HTTP 400.
 */
public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
