package com.pricingia.saas.modules.webhook.domain;

import com.pricingia.saas.common.exception.UnauthorizedException;

public class WebhookUnauthorizedException extends UnauthorizedException {
	public WebhookUnauthorizedException() {
		super("Invalid Shopify webhook HMAC");
	}
}
