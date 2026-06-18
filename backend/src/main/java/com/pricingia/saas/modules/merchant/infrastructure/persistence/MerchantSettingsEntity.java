package com.pricingia.saas.modules.merchant.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "merchant_settings", schema = "pricingia")
public class MerchantSettingsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "shop_domain", nullable = false, length = 255, unique = true)
	private String shopDomain;

	@Column(name = "currency", nullable = false, length = 8)
	private String currency;

	@Column(name = "min_margin_percentage", nullable = false, precision = 6, scale = 2)
	private BigDecimal minMarginPercentage;

	@Column(name = "max_price_increase_percentage", nullable = false, precision = 6, scale = 2)
	private BigDecimal maxPriceIncreasePercentage;

	@Column(name = "automation_enabled", nullable = false)
	private boolean automationEnabled;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected MerchantSettingsEntity() {
	}

	public MerchantSettingsEntity(String shopDomain, String currency, BigDecimal minMarginPercentage,
			BigDecimal maxPriceIncreasePercentage, boolean automationEnabled, Instant now) {
		this.shopDomain = shopDomain;
		this.currency = currency;
		this.minMarginPercentage = minMarginPercentage;
		this.maxPriceIncreasePercentage = maxPriceIncreasePercentage;
		this.automationEnabled = automationEnabled;
		this.createdAt = now;
		this.updatedAt = now;
	}

	public Long getId() {
		return id;
	}

	public String getShopDomain() {
		return shopDomain;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getMinMarginPercentage() {
		return minMarginPercentage;
	}

	public void setMinMarginPercentage(BigDecimal minMarginPercentage) {
		this.minMarginPercentage = minMarginPercentage;
	}

	public BigDecimal getMaxPriceIncreasePercentage() {
		return maxPriceIncreasePercentage;
	}

	public void setMaxPriceIncreasePercentage(BigDecimal maxPriceIncreasePercentage) {
		this.maxPriceIncreasePercentage = maxPriceIncreasePercentage;
	}

	public boolean isAutomationEnabled() {
		return automationEnabled;
	}

	public void setAutomationEnabled(boolean automationEnabled) {
		this.automationEnabled = automationEnabled;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
