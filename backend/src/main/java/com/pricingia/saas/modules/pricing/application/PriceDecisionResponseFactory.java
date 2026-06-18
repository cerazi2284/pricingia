package com.pricingia.saas.modules.pricing.application;

import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductRepository;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionEntity;
import com.pricingia.saas.modules.pricing.presentation.dto.PriceDecisionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PriceDecisionResponseFactory {

	private final MonitoredProductRepository productRepository;

	public PriceDecisionResponseFactory(MonitoredProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public PriceDecisionResponse toResponse(PriceDecisionEntity entity) {
		return toResponses(List.of(entity)).getFirst();
	}

	public List<PriceDecisionResponse> toResponses(List<PriceDecisionEntity> entities) {
		Set<Long> productIds = entities.stream()
				.map(PriceDecisionEntity::getMonitoredProductId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());

		Map<Long, MonitoredProductEntity> productsById = productRepository.findAllById(productIds).stream()
				.collect(Collectors.toMap(MonitoredProductEntity::getId, Function.identity()));

		return entities.stream()
				.map(entity -> {
					String productTitle = "Unknown product";
					if (entity.getMonitoredProductId() != null) {
						MonitoredProductEntity product = productsById.get(entity.getMonitoredProductId());
						if (product != null) {
							productTitle = product.getTitle();
						}
					}
					return PriceDecisionResponse.from(entity, productTitle, resolveDecisionType(entity));
				})
				.toList();
	}

	private static String resolveDecisionType(PriceDecisionEntity entity) {
		BigDecimal oldPrice = entity.getOldPrice();
		BigDecimal suggested = entity.getSuggestedPrice();
		int cmp = suggested.compareTo(oldPrice);
		if (cmp > 0) {
			return "INCREASE";
		}
		if (cmp < 0) {
			return "DECREASE";
		}
		return "HOLD";
	}
}
