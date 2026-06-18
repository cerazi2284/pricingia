package com.pricingia.saas.modules.pricing.application;

import com.pricingia.saas.modules.merchant.application.MerchantSettingsService;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MerchantSettingsEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductEntity;
import com.pricingia.saas.modules.merchant.infrastructure.persistence.MonitoredProductRepository;
import com.pricingia.saas.modules.pricing.domain.PricingInput;
import com.pricingia.saas.modules.pricing.domain.PricingOutcome;
import com.pricingia.saas.modules.pricing.domain.PricingRule;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionEntity;
import com.pricingia.saas.modules.pricing.infrastructure.persistence.PriceDecisionRepository;
import com.pricingia.saas.modules.webhook.domain.WebhookStatus;
import com.pricingia.saas.modules.webhook.infrastructure.persistence.ShopifyWebhookEventEntity;
import com.pricingia.saas.modules.webhook.infrastructure.persistence.ShopifyWebhookEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Consumes a received webhook event and produces a price decision via the deterministic
 * {@link PricingRule}. No external Shopify call and no real price update yet.
 */
@Service
public class ProcessShopifyWebhookEventService {

	private static final Logger log = LoggerFactory.getLogger(ProcessShopifyWebhookEventService.class);
	private static final String[] PRODUCT_ID_FIELDS = {"shopify_product_id", "product_id", "id"};

	private final ShopifyWebhookEventRepository eventRepository;
	private final MerchantSettingsService merchantSettingsService;
	private final MonitoredProductRepository productRepository;
	private final PriceDecisionRepository priceDecisionRepository;

	public ProcessShopifyWebhookEventService(
			ShopifyWebhookEventRepository eventRepository,
			MerchantSettingsService merchantSettingsService,
			MonitoredProductRepository productRepository,
			PriceDecisionRepository priceDecisionRepository) {
		this.eventRepository = eventRepository;
		this.merchantSettingsService = merchantSettingsService;
		this.productRepository = productRepository;
		this.priceDecisionRepository = priceDecisionRepository;
	}

	@Transactional
	public void handle(Long eventId, String payload) {
		markStatus(eventId, WebhookStatus.PROCESSING, null);

		MerchantSettingsEntity merchant = merchantSettingsService.getCurrent();
		Optional<MonitoredProductEntity> product = resolveProduct(merchant.getId(), payload);

		if (product.isEmpty()) {
			log.warn("No monitored product resolved for eventId={}; marking event FAILED", eventId);
			markStatus(eventId, WebhookStatus.FAILED, Instant.now());
			return;
		}

		MonitoredProductEntity p = product.get();
		PricingInput input = new PricingInput(
				p.getCurrentPrice(),
				p.getCost(),
				merchant.getMinMarginPercentage(),
				merchant.getMaxPriceIncreasePercentage(),
				merchant.isAutomationEnabled()
		);

		PricingOutcome outcome = PricingRule.evaluate(input);

		PriceDecisionEntity decision = new PriceDecisionEntity(
				merchant.getId(),
				p.getId(),
				outcome.oldPrice(),
				outcome.suggestedPrice(),
				outcome.reason(),
				outcome.status(),
				eventId,
				Instant.now()
		);
		priceDecisionRepository.save(decision);

		markStatus(eventId, WebhookStatus.PROCESSED, Instant.now());
		log.info("Generated price decision (status={}) for productId={} from eventId={}",
				outcome.status(), p.getId(), eventId);
	}

	private Optional<MonitoredProductEntity> resolveProduct(Long merchantSettingsId, String payload) {
		for (String candidate : extractProductIdCandidates(payload)) {
			Optional<MonitoredProductEntity> byId =
					productRepository.findFirstByMerchantSettingsIdAndShopifyProductIdOrderByIdAsc(merchantSettingsId, candidate);
			if (byId.isPresent()) {
				return byId;
			}
		}

		List<MonitoredProductEntity> active = productRepository.findByMerchantSettingsIdAndActiveTrue(merchantSettingsId);
		return active.stream().findFirst();
	}

	/**
	 * Extracts a product id from the raw JSON payload without a JSON library
	 * (keeps the consumer dependency-free for the MVP). Matches {@code "field":"value"}
	 * or {@code "field":value} for the known id fields.
	 */
	private List<String> extractProductIdCandidates(String payload) {
		if (payload == null || payload.isBlank()) {
			return List.of();
		}
		for (String field : PRODUCT_ID_FIELDS) {
			Pattern pattern = Pattern.compile("\"" + Pattern.quote(field) + "\"\\s*:\\s*\"?([^\",}\\s]+)\"?");
			Matcher matcher = pattern.matcher(payload);
			if (matcher.find()) {
				return List.of(matcher.group(1));
			}
		}
		return List.of();
	}

	private void markStatus(Long eventId, WebhookStatus status, Instant processedAt) {
		eventRepository.findById(eventId).ifPresent(event -> applyStatus(event, status, processedAt));
	}

	private static void applyStatus(ShopifyWebhookEventEntity event, WebhookStatus status, Instant processedAt) {
		event.setStatus(status);
		if (processedAt != null) {
			event.setProcessedAt(processedAt);
		}
	}
}
