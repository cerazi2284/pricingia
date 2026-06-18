package com.pricingia.saas.modules.merchant.application;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the demo merchant settings at startup if they do not exist yet.
 * Local-dev convenience; real provisioning comes with merchant onboarding later.
 */
@Component
public class MerchantDemoInitializer implements ApplicationRunner {

	private final MerchantSettingsService merchantSettingsService;

	public MerchantDemoInitializer(MerchantSettingsService merchantSettingsService) {
		this.merchantSettingsService = merchantSettingsService;
	}

	@Override
	public void run(ApplicationArguments args) {
		merchantSettingsService.getCurrent();
	}
}
