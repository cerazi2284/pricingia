export type MerchantSettings = {
  id: number;
  shopDomain: string;
  currency: string;
  minMarginPercentage: number;
  maxPriceIncreasePercentage: number;
  automationEnabled: boolean;
  createdAt: string;
  updatedAt: string;
};

export type UpdateMerchantSettingsRequest = {
  currency: string;
  minMarginPercentage: number;
  maxPriceIncreasePercentage: number;
  automationEnabled: boolean;
};

export type Product = {
  id: number;
  merchantSettingsId: number;
  shopifyProductId: string;
  title: string;
  sku: string;
  currentPrice: number;
  cost: number;
  inventoryQuantity: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
};

export type PriceDecisionStatus = "SUGGESTED" | "APPROVED" | "REJECTED";

export type PriceDecision = {
  id: number;
  merchantSettingsId: number;
  monitoredProductId: number;
  productTitle: string;
  decisionType: string;
  oldPrice: number;
  suggestedPrice: number;
  reason: string;
  status: PriceDecisionStatus;
  sourceEventId: number;
  createdAt: string;
  updatedAt: string;
};

export type AnalyticsSummary = {
  shopDomain: string;
  currency: string;
  productsMonitored: number;
  priceDecisions: number;
  suggestions: number;
  approved: number;
  rejected: number;
  webhookEventsReceived: number;
  potentialUplift: number;
};
