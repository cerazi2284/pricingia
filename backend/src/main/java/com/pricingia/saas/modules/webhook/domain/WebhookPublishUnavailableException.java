package com.pricingia.saas.modules.webhook.domain;

import com.pricingia.saas.common.exception.ServiceUnavailableException;

public class WebhookPublishUnavailableException extends ServiceUnavailableException {
	public WebhookPublishUnavailableException(Throwable cause) {
		super("Failed to publish webhook event", cause);
	}
}
