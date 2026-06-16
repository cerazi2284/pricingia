export type DashboardMetric = {
  key: "revenueProtected" | "priceDecisions" | "productsMonitored" | "aiSuggestions" | "marginUplift";
  label: string;
  value: string;
  deltaLabel: string;
};

export const dashboardMetrics: DashboardMetric[] = [
  {
    key: "revenueProtected",
    label: "Revenue protected",
    value: "$42,180",
    deltaLabel: "+$3,240 this week",
  },
  {
    key: "priceDecisions",
    label: "Price decisions",
    value: "128",
    deltaLabel: "+18 last 7 days",
  },
  {
    key: "productsMonitored",
    label: "Products monitored",
    value: "412",
    deltaLabel: "across 3 collections",
  },
  {
    key: "aiSuggestions",
    label: "AI suggestions",
    value: "36",
    deltaLabel: "pending review",
  },
  {
    key: "marginUplift",
    label: "Margin uplift",
    value: "+2.4%",
    deltaLabel: "estimated",
  },
];

