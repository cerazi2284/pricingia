package com.pricingia.saas.modules.analytics.presentation;

import com.pricingia.saas.modules.analytics.application.AnalyticsService;
import com.pricingia.saas.modules.analytics.presentation.dto.AnalyticsSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

	private final AnalyticsService service;

	public AnalyticsController(AnalyticsService service) {
		this.service = service;
	}

	@GetMapping("/summary")
	public AnalyticsSummaryResponse summary() {
		return service.summary();
	}
}
