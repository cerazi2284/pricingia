package com.pricingia.saas.modules.pricing.application;

import com.pricingia.saas.common.exception.BusinessException;
import com.pricingia.saas.common.exception.NotFoundException;
import com.pricingia.saas.modules.pricing.domain.PriceDecisionStatus;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionEntity;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PriceDecisionService {

	private final PriceDecisionRepository repository;

	public PriceDecisionService(PriceDecisionRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<PriceDecisionEntity> list() {
		return repository.findAllByOrderByCreatedAtDesc();
	}

	@Transactional(readOnly = true)
	public PriceDecisionEntity getById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new NotFoundException("Price decision not found: " + id));
	}

	@Transactional
	public PriceDecisionEntity approve(Long id) {
		return transition(id, PriceDecisionStatus.APPROVED);
	}

	@Transactional
	public PriceDecisionEntity reject(Long id) {
		return transition(id, PriceDecisionStatus.REJECTED);
	}

	private PriceDecisionEntity transition(Long id, PriceDecisionStatus target) {
		PriceDecisionEntity decision = getById(id);
		if (decision.getStatus() != PriceDecisionStatus.SUGGESTED) {
			throw new BusinessException(
					"Only SUGGESTED decisions can be %s. Current status: %s"
							.formatted(target.name().toLowerCase(), decision.getStatus()));
		}
		decision.setStatus(target);
		decision.setUpdatedAt(Instant.now());
		return repository.save(decision);
	}
}
