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
@Table(name = "monitored_products", schema = "pricingia")
public class MonitoredProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "merchant_settings_id", nullable = false)
	private Long merchantSettingsId;

	@Column(name = "shopify_product_id", nullable = false, length = 128)
	private String shopifyProductId;

	@Column(name = "title", nullable = false, length = 512)
	private String title;

	@Column(name = "sku", length = 255)
	private String sku;

	@Column(name = "current_price", nullable = false, precision = 19, scale = 4)
	private BigDecimal currentPrice;

	@Column(name = "cost", nullable = false, precision = 19, scale = 4)
	private BigDecimal cost;

	@Column(name = "inventory_quantity", nullable = false)
	private int inventoryQuantity;

	@Column(name = "active", nullable = false)
	private boolean active;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected MonitoredProductEntity() {
	}

	public MonitoredProductEntity(Long merchantSettingsId, String shopifyProductId, String title, String sku,
			BigDecimal currentPrice, BigDecimal cost, int inventoryQuantity, boolean active, Instant now) {
		this.merchantSettingsId = merchantSettingsId;
		this.shopifyProductId = shopifyProductId;
		this.title = title;
		this.sku = sku;
		this.currentPrice = currentPrice;
		this.cost = cost;
		this.inventoryQuantity = inventoryQuantity;
		this.active = active;
		this.createdAt = now;
		this.updatedAt = now;
	}

	public Long getId() {
		return id;
	}

	public Long getMerchantSettingsId() {
		return merchantSettingsId;
	}

	public String getShopifyProductId() {
		return shopifyProductId;
	}

	public String getTitle() {
		return title;
	}

	public String getSku() {
		return sku;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public int getInventoryQuantity() {
		return inventoryQuantity;
	}

	public boolean isActive() {
		return active;
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
