export type PricingDecisionRow = {
  id: string;
  productTitle: string;
  decision: "Increase" | "Decrease" | "Hold";
  fromPriceUsd: number;
  toPriceUsd: number;
  reason: string;
  createdAtIso: string;
  status: "Applied" | "Suggested" | "Rejected";
};

export const pricingDecisionsMock: PricingDecisionRow[] = [
  {
    id: "dec_2001",
    productTitle: "Running Shorts",
    decision: "Increase",
    fromPriceUsd: 49,
    toPriceUsd: 54,
    reason: "High velocity + low inventory (16). Protect margin.",
    createdAtIso: "2026-06-16T11:05:00Z",
    status: "Suggested",
  },
  {
    id: "dec_2002",
    productTitle: "Premium Hoodie",
    decision: "Hold",
    fromPriceUsd: 79,
    toPriceUsd: 79,
    reason: "Stable conversion. No change recommended.",
    createdAtIso: "2026-06-16T10:20:00Z",
    status: "Applied",
  },
  {
    id: "dec_2003",
    productTitle: "Everyday Tee",
    decision: "Decrease",
    fromPriceUsd: 29,
    toPriceUsd: 27,
    reason: "Inventory is high. Improve sell-through.",
    createdAtIso: "2026-06-15T20:10:00Z",
    status: "Applied",
  },
];

