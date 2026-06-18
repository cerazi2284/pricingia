import { useState } from "react";
import { productsApi } from "@/api";
import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TBody, THead, Td, Th, Tr } from "@/components/ui/table";
import { useFetch } from "@/hooks/useFetch";
import { formatMoney } from "@/lib/format";

export function ProductsPage() {
  const { data: products, loading, error, reload } = useFetch(() => productsApi.list(), []);
  const [seeding, setSeeding] = useState(false);

  async function loadDemo() {
    setSeeding(true);
    try {
      await productsApi.createDemo();
      await reload();
    } finally {
      setSeeding(false);
    }
  }

  return (
    <div className="min-h-screen">
      <Topbar title="Products" subtitle="Monitored products · live API" />

      <div className="px-6 py-6 space-y-4">
        <div className="flex justify-end">
          <button
            onClick={() => void loadDemo()}
            disabled={seeding}
            className="h-9 rounded-lg bg-slate-900 px-4 text-sm text-white hover:bg-slate-800 disabled:opacity-50"
          >
            {seeding ? "Loading…" : "Load demo products"}
          </button>
        </div>

        {error && (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <Card>
          <CardHeader>
            <CardTitle>Products</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-sm text-slate-500">Loading products…</div>
            ) : !products?.length ? (
              <div className="text-sm text-slate-500">
                No products yet. Click &quot;Load demo products&quot; to seed data.
              </div>
            ) : (
              <div className="overflow-auto">
                <Table>
                  <THead>
                    <Tr>
                      <Th>Title</Th>
                      <Th>SKU</Th>
                      <Th className="text-right">Price</Th>
                      <Th className="text-right">Cost</Th>
                      <Th className="text-right">Inventory</Th>
                      <Th>Status</Th>
                    </Tr>
                  </THead>
                  <TBody>
                    {products.map((p) => (
                      <Tr key={p.id}>
                        <Td className="font-medium">{p.title}</Td>
                        <Td className="text-slate-600">{p.sku}</Td>
                        <Td className="text-right">{formatMoney(p.currentPrice)}</Td>
                        <Td className="text-right text-slate-600">{formatMoney(p.cost)}</Td>
                        <Td className="text-right">{p.inventoryQuantity}</Td>
                        <Td>
                          <span
                            className={
                              p.active
                                ? "inline-flex items-center rounded-full bg-emerald-50 px-2 py-0.5 text-xs text-emerald-700"
                                : "inline-flex items-center rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-700"
                            }
                          >
                            {p.active ? "Monitored" : "Inactive"}
                          </span>
                        </Td>
                      </Tr>
                    ))}
                  </TBody>
                </Table>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
