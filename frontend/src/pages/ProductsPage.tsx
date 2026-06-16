import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TBody, THead, Td, Th, Tr } from "@/components/ui/table";
import { productsMock } from "@/mocks/products";

function usd(amount: number) {
  return `$${amount.toFixed(2)}`;
}

export function ProductsPage() {
  return (
    <div className="min-h-screen">
      <Topbar title="Products" subtitle="Monitored products (mock data)" />

      <div className="px-6 py-6">
        <Card>
          <CardHeader>
            <CardTitle>Products</CardTitle>
          </CardHeader>
          <CardContent>
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
                  {productsMock.map((p) => (
                    <Tr key={p.id}>
                      <Td className="font-medium">{p.title}</Td>
                      <Td className="text-slate-600">{p.sku}</Td>
                      <Td className="text-right">{usd(p.priceUsd)}</Td>
                      <Td className="text-right text-slate-600">{usd(p.costUsd)}</Td>
                      <Td className="text-right">{p.inventory}</Td>
                      <Td>
                        <span
                          className={
                            p.status === "Monitored"
                              ? "inline-flex items-center rounded-full bg-emerald-50 px-2 py-0.5 text-xs text-emerald-700"
                              : "inline-flex items-center rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-700"
                          }
                        >
                          {p.status}
                        </span>
                      </Td>
                    </Tr>
                  ))}
                </TBody>
              </Table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

