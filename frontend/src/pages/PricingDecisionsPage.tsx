import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TBody, THead, Td, Th, Tr } from "@/components/ui/table";
import { pricingDecisionsMock } from "@/mocks/pricing-decisions";

function usd(amount: number) {
  return `$${amount.toFixed(2)}`;
}

export function PricingDecisionsPage() {
  return (
    <div className="min-h-screen">
      <Topbar title="Pricing Decisions" subtitle="Applied and suggested decisions (mock data)" />

      <div className="px-6 py-6 space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>Decisions</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-auto">
              <Table>
                <THead>
                  <Tr>
                    <Th>Product</Th>
                    <Th>Decision</Th>
                    <Th className="text-right">From</Th>
                    <Th className="text-right">To</Th>
                    <Th>Status</Th>
                    <Th>Reason</Th>
                  </Tr>
                </THead>
                <TBody>
                  {pricingDecisionsMock.map((d) => (
                    <Tr key={d.id}>
                      <Td className="font-medium">{d.productTitle}</Td>
                      <Td>{d.decision}</Td>
                      <Td className="text-right">{usd(d.fromPriceUsd)}</Td>
                      <Td className="text-right">{usd(d.toPriceUsd)}</Td>
                      <Td className="text-slate-600">{d.status}</Td>
                      <Td className="text-slate-600">{d.reason}</Td>
                    </Tr>
                  ))}
                </TBody>
              </Table>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>AI suggestion (visual placeholder)</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm text-slate-700">
              Slot reservado para mostrar “AI suggestion” com explicabilidade e guardrails.
            </div>
            <div className="mt-2 text-xs text-slate-500">
              Sem IA real nesta etapa. Dados continuam em `src/mocks`.
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

