import { api } from "@/lib/api-client";
import type {
  AnalyticsSummary,
  MerchantSettings,
  PriceDecision,
  Product,
  UpdateMerchantSettingsRequest,
} from "@/types/api";

export const analyticsApi = {
  getSummary: () => api<AnalyticsSummary>("/api/analytics/summary"),
};

export const merchantApi = {
  getSettings: () => api<MerchantSettings>("/api/merchant/settings"),
  updateSettings: (body: UpdateMerchantSettingsRequest) =>
    api<MerchantSettings>("/api/merchant/settings", {
      method: "PUT",
      body: JSON.stringify(body),
    }),
};

export const productsApi = {
  list: () => api<Product[]>("/api/products"),
  createDemo: () =>
    api<Product[]>("/api/products/demo", {
      method: "POST",
    }),
};

export const pricingApi = {
  listDecisions: () => api<PriceDecision[]>("/api/pricing/decisions"),
  approve: (id: number) =>
    api<PriceDecision>(`/api/pricing/decisions/${id}/approve`, { method: "POST" }),
  reject: (id: number) =>
    api<PriceDecision>(`/api/pricing/decisions/${id}/reject`, { method: "POST" }),
};
