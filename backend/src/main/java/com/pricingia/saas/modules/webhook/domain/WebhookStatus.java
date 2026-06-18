package com.pricingia.saas.modules.webhook.domain;

public enum WebhookStatus {
	RECEIVED,
	PUBLISHED,
	DUPLICATE,
	PROCESSING,
	PROCESSED,
	FAILED
}
