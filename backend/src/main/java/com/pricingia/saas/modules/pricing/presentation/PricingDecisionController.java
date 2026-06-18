package com.pricingia.saas.modules.pricing.presentation;

import com.pricingia.saas.modules.pricing.application.PriceDecisionResponseFactory;
import com.pricingia.saas.modules.pricing.application.PriceDecisionService;
import com.pricingia.saas.modules.pricing.presentation.dto.PriceDecisionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pricing/decisions")
public class PricingDecisionController {

	private final PriceDecisionService service;
	private final PriceDecisionResponseFactory responseFactory;

	public PricingDecisionController(PriceDecisionService service, PriceDecisionResponseFactory responseFactory) {
		this.service = service;
		this.responseFactory = responseFactory;
	}

	@GetMapping
	public List<PriceDecisionResponse> list() {
		return responseFactory.toResponses(service.list());
	}

	@GetMapping("/{id}")
	public PriceDecisionResponse getById(@PathVariable Long id) {
		return responseFactory.toResponse(service.getById(id));
	}

	@PostMapping("/{id}/approve")
	public PriceDecisionResponse approve(@PathVariable Long id) {
		return responseFactory.toResponse(service.approve(id));
	}

	@PostMapping("/{id}/reject")
	public PriceDecisionResponse reject(@PathVariable Long id) {
		return responseFactory.toResponse(service.reject(id));
	}
}
