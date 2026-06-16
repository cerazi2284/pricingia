export type ProductRow = {
  id: string;
  title: string;
  sku: string;
  priceUsd: number;
  costUsd: number;
  inventory: number;
  status: "Monitored" | "Paused";
};

export const productsMock: ProductRow[] = [
  {
    id: "prod_1001",
    title: "Premium Hoodie",
    sku: "HD-PRM-BLK",
    priceUsd: 79.0,
    costUsd: 31.5,
    inventory: 42,
    status: "Monitored",
  },
  {
    id: "prod_1002",
    title: "Everyday Tee",
    sku: "TS-EVD-WHT",
    priceUsd: 29.0,
    costUsd: 9.2,
    inventory: 180,
    status: "Monitored",
  },
  {
    id: "prod_1003",
    title: "Running Shorts",
    sku: "SH-RUN-GRY",
    priceUsd: 49.0,
    costUsd: 18.0,
    inventory: 16,
    status: "Monitored",
  },
  {
    id: "prod_1004",
    title: "Accessory Pack",
    sku: "AC-PCK-001",
    priceUsd: 19.0,
    costUsd: 6.1,
    inventory: 0,
    status: "Paused",
  },
];

