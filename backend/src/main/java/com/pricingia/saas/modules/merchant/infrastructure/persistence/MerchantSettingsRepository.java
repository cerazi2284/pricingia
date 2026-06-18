package com.pricingia.saas.modules.merchant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantSettingsRepository extends JpaRepository<MerchantSettingsEntity, Long> {

	Optional<MerchantSettingsEntity> findByShopDomain(String shopDomain);
}
