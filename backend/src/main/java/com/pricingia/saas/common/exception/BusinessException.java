package com.pricingia.saas.common.exception;

/**
 * Maps to HTTP 422 (semantically valid request that violates a business rule).
 */
public class BusinessException extends RuntimeException {

	public BusinessException(String message) {
		super(message);
	}
}
