package com.pricingia.saas.modules.merchant.presentation;

import com.pricingia.saas.modules.merchant.application.MonitoredProductService;
import com.pricingia.saas.modules.merchant.presentation.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final MonitoredProductService service;

	public ProductController(MonitoredProductService service) {
		this.service = service;
	}

	@GetMapping
	public List<ProductResponse> list() {
		return service.listForCurrentMerchant().stream()
				.map(ProductResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public ProductResponse getById(@PathVariable Long id) {
		return ProductResponse.from(service.getById(id));
	}

	/**
	 * Temporary local-dev endpoint to seed mock products. Not part of the real product API.
	 */
	@PostMapping("/demo")
	@ResponseStatus(HttpStatus.CREATED)
	public List<ProductResponse> createDemo() {
		return service.createDemoProducts().stream()
				.map(ProductResponse::from)
				.toList();
	}
}
