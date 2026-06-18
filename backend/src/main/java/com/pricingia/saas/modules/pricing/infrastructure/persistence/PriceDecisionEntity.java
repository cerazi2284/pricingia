package com.pricingia.saas.modules.pricing.infrastructure.persistence;

import com.pricingia.saas.modules.pricing.domain.PriceDecisionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_decisions", schema = "pricingia")
public class PriceDecisionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "merchant_settings_id", nullable = false)
	private Long merchantSettingsId;

	@Column(name = "monitored_product_id")
	private Long monitoredProductId;

	@Column(name = "old_price", nullable = false, precision = 19, scale = 4)
	private BigDecimal oldPrice;

	@Column(name = "suggested_price", nullable = false, precision = 19, scale = 4)
	private BigDecimal suggestedPrice;

	@Column(name = "reason", nullable = false, length = 1024)
	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private PriceDecisionStatus status;

	@Column(name = "source_event_id")
	private Long sourceEventId;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected PriceDecisionEntity() {
	}

	public PriceDecisionEntity(Long merchantSettingsId, Long monitoredProductId, BigDecimal oldPrice,
			BigDecimal suggestedPrice, String reason, PriceDecisionStatus status, Long sourceEventId, Instant now) {
		this.merchantSettingsId = merchantSettingsId;
		this.monitoredProductId = monitoredProductId;
		this.oldPrice = oldPrice;
		this.suggestedPrice = suggestedPrice;
		this.reason = reason;
		this.status = status;
		this.sourceEventId = sourceEventId;
		this.createdAt = now;
		this.updatedAt = now;
	}

	public Long getId() {
		return id;
	}

	public Long getMerchantSettingsId() {
		return merchantSettingsId;
	}

	public Long getMonitoredProductId() {
		return monitoredProductId;
	}

	public BigDecimal getOldPrice() {
		return oldPrice;
	}

	public BigDecimal getSuggestedPrice() {
		return suggestedPrice;
	}

	public String getReason() {
		return reason;
	}

	public PriceDecisionStatus getStatus() {
		return status;
	}

	public void setStatus(PriceDecisionStatus status) {
		this.status = status;
	}

	public Long getSourceEventId() {
		return sourceEventId;
	}

	/**
	 * Potential revenue uplift per unit (suggested minus old price). Never negative.
	 */
	public BigDecimal computeUplift() {
		BigDecimal diff = suggestedPrice.subtract(oldPrice);
		return diff.signum() > 0 ? diff : BigDecimal.ZERO;
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
