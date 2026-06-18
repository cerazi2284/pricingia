package com.pricingia.saas.modules.merchant.presentation.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateMerchantSettingsRequest(
		@NotBlank String currency,

		@NotNull
		@DecimalMin(value = "0.0", inclusive = true)
		@DecimalMax(value = "100.0", inclusive = true)
		BigDecimal minMarginPercentage,

		@NotNull
		@DecimalMin(value = "0.0", inclusive = true)
		@DecimalMax(value = "100.0", inclusive = true)
		BigDecimal maxPriceIncreasePercentage,

		@NotNull Boolean automationEnabled
) {
}
