package com.pricingia.saas.modules.webhook.domain;

import com.pricingia.saas.common.exception.BadRequestException;

public class WebhookBadRequestException extends BadRequestException {
	public WebhookBadRequestException(String message) {
		super(message);
	}
}
