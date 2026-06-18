package com.pricingia.saas.modules.pricing.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Deterministic MVP pricing rule (no AI yet).
 *
 * <p>Rules:</p>
 * <ul>
 *   <li>Automation disabled → FAILED, no price change suggested.</li>
 *   <li>Invalid price/cost → FAILED.</li>
 *   <li>Current margin already at/above minimum → FAILED (no action needed).</li>
 *   <li>Margin below minimum → suggest a price increase to reach the minimum margin,
 *       capped by the max allowed increase percentage → SUGGESTED.</li>
 * </ul>
 *
 * <p>All money math uses {@link BigDecimal}.</p>
 */
public final class PricingRule {

	private static final int CALC_SCALE = 6;
	private static final int MONEY_SCALE = 4;
	private static final BigDecimal HUNDRED = new BigDecimal("100");

	private PricingRule() {
	}

	public static PricingOutcome evaluate(PricingInput input) {
		BigDecimal currentPrice = input.currentPrice();
		BigDecimal cost = input.cost();

		if (!input.automationEnabled()) {
			return failed(currentPrice, "Automation disabled for this merchant");
		}

		if (currentPrice == null || cost == null
				|| currentPrice.signum() <= 0 || cost.signum() < 0) {
			return failed(currentPrice, "Invalid price or cost");
		}

		BigDecimal currentMargin = currentPrice.subtract(cost)
				.divide(currentPrice, CALC_SCALE, RoundingMode.HALF_UP)
				.multiply(HUNDRED);

		if (currentMargin.compareTo(input.minMarginPercentage()) >= 0) {
			return failed(currentPrice, "Current margin %s%% already meets the minimum %s%%"
					.formatted(scale2(currentMargin), scale2(input.minMarginPercentage())));
		}

		BigDecimal targetPrice = targetPriceForMargin(cost, input.minMarginPercentage());
		BigDecimal maxAllowedPrice = maxAllowedPrice(currentPrice, input.maxPriceIncreasePercentage());

		BigDecimal suggested = targetPrice.min(maxAllowedPrice).setScale(MONEY_SCALE, RoundingMode.HALF_UP);

		String reason;
		if (targetPrice.compareTo(maxAllowedPrice) > 0) {
			reason = "Margin %s%% below minimum %s%%; increase capped at %s%%"
					.formatted(scale2(currentMargin), scale2(input.minMarginPercentage()),
							scale2(input.maxPriceIncreasePercentage()));
		} else {
			reason = "Margin %s%% below minimum %s%%; price raised to reach target margin"
					.formatted(scale2(currentMargin), scale2(input.minMarginPercentage()));
		}

		return new PricingOutcome(PriceDecisionStatus.SUGGESTED,
				currentPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP), suggested, reason);
	}

	private static BigDecimal targetPriceForMargin(BigDecimal cost, BigDecimal minMarginPercentage) {
		// price such that (price - cost) / price = minMargin/100  =>  price = cost / (1 - minMargin/100)
		BigDecimal marginFraction = minMarginPercentage.divide(HUNDRED, CALC_SCALE, RoundingMode.HALF_UP);
		BigDecimal denominator = BigDecimal.ONE.subtract(marginFraction);
		if (denominator.signum() <= 0) {
			// minMargin >= 100% is not achievable; fall back to cost (caller caps by max increase).
			return cost;
		}
		return cost.divide(denominator, CALC_SCALE, RoundingMode.HALF_UP);
	}

	private static BigDecimal maxAllowedPrice(BigDecimal currentPrice, BigDecimal maxIncreasePercentage) {
		BigDecimal factor = BigDecimal.ONE.add(maxIncreasePercentage.divide(HUNDRED, CALC_SCALE, RoundingMode.HALF_UP));
		return currentPrice.multiply(factor);
	}

	private static PricingOutcome failed(BigDecimal currentPrice, String reason) {
		BigDecimal price = currentPrice == null
				? BigDecimal.ZERO.setScale(MONEY_SCALE)
				: currentPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
		return new PricingOutcome(PriceDecisionStatus.FAILED, price, price, reason);
	}

	private static BigDecimal scale2(BigDecimal value) {
		return value.setScale(2, RoundingMode.HALF_UP);
	}
}
