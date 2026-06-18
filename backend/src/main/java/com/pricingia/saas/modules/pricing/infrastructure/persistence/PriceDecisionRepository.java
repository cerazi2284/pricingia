package com.pricingia.saas.modules.pricing.infrastructure.persistence;

import com.pricingia.saas.modules.pricing.domain.PriceDecisionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceDecisionRepository extends JpaRepository<PriceDecisionEntity, Long> {

	List<PriceDecisionEntity> findAllByOrderByCreatedAtDesc();

	long countByStatus(PriceDecisionStatus status);
}
