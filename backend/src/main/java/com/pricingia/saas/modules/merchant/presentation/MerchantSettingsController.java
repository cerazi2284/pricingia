package com.pricingia.saas.modules.merchant.presentation;

import com.pricingia.saas.modules.merchant.application.MerchantSettingsService;
import com.pricingia.saas.modules.merchant.presentation.dto.MerchantSettingsResponse;
import com.pricingia.saas.modules.merchant.presentation.dto.UpdateMerchantSettingsRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merchant/settings")
public class MerchantSettingsController {

	private final MerchantSettingsService service;

	public MerchantSettingsController(MerchantSettingsService service) {
		this.service = service;
	}

	@GetMapping
	public MerchantSettingsResponse get() {
		return MerchantSettingsResponse.from(service.getCurrent());
	}

	@PutMapping
	public MerchantSettingsResponse update(@Valid @RequestBody UpdateMerchantSettingsRequest request) {
		return MerchantSettingsResponse.from(service.update(
				request.currency(),
				request.minMarginPercentage(),
				request.maxPriceIncreasePercentage(),
				request.automationEnabled()
		));
	}
}
