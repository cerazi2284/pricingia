package com.pricingia.saas.modules.merchant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoredProductRepository extends JpaRepository<MonitoredProductEntity, Long> {

	List<MonitoredProductEntity> findByMerchantSettingsId(Long merchantSettingsId);

	List<MonitoredProductEntity> findByMerchantSettingsIdAndActiveTrue(Long merchantSettingsId);

	Optional<MonitoredProductEntity> findFirstByMerchantSettingsIdAndShopifyProductIdOrderByIdAsc(
			Long merchantSettingsId, String shopifyProductId);

	long countByActiveTrue();
}
