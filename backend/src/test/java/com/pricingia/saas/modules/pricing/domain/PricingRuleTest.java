package com.pricingia.saas.modules.pricing.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PricingRuleTest {

	@Test
	void shouldSuggestIncreaseWhenMarginBelowMinimum() {
		PricingInput input = new PricingInput(
				new BigDecimal("50.00"), new BigDecimal("40.00"),
				new BigDecimal("30.00"), new BigDecimal("50.00"), true);

		PricingOutcome outcome = PricingRule.evaluate(input);

		assertThat(outcome.status()).isEqualTo(PriceDecisionStatus.SUGGESTED);
		// target price to reach 30% margin: 40 / 0.70 = 57.1429
		assertThat(outcome.suggestedPrice()).isEqualByComparingTo("57.1429");
		assertThat(outcome.suggestedPrice()).isGreaterThan(outcome.oldPrice());
	}

	@Test
	void shouldCapSuggestedPriceByMaxIncreasePercentage() {
		PricingInput input = new PricingInput(
				new BigDecimal("50.00"), new BigDecimal("40.00"),
				new BigDecimal("30.00"), new BigDecimal("5.00"), true);

		PricingOutcome outcome = PricingRule.evaluate(input);

		assertThat(outcome.status()).isEqualTo(PriceDecisionStatus.SUGGESTED);
		// capped at +5% -> 50 * 1.05 = 52.50
		assertThat(outcome.suggestedPrice()).isEqualByComparingTo("52.5000");
		assertThat(outcome.reason()).contains("capped");
	}

	@Test
	void shouldFailWhenAutomationDisabled() {
		PricingInput input = new PricingInput(
				new BigDecimal("50.00"), new BigDecimal("40.00"),
				new BigDecimal("30.00"), new BigDecimal("15.00"), false);

		PricingOutcome outcome = PricingRule.evaluate(input);

		assertThat(outcome.status()).isEqualTo(PriceDecisionStatus.FAILED);
		assertThat(outcome.suggestedPrice()).isEqualByComparingTo(outcome.oldPrice());
		assertThat(outcome.reason()).contains("Automation disabled");
	}

	@Test
	void shouldFailWhenMarginAlreadyMeetsMinimum() {
		PricingInput input = new PricingInput(
				new BigDecimal("100.00"), new BigDecimal("40.00"),
				new BigDecimal("30.00"), new BigDecimal("15.00"), true);

		PricingOutcome outcome = PricingRule.evaluate(input);

		assertThat(outcome.status()).isEqualTo(PriceDecisionStatus.FAILED);
		assertThat(outcome.suggestedPrice()).isEqualByComparingTo(outcome.oldPrice());
	}
}
