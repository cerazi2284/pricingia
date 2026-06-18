import { useState } from "react";
import { pricingApi } from "@/api";
import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TBody, THead, Td, Th, Tr } from "@/components/ui/table";
import { useFetch } from "@/hooks/useFetch";
import { formatMoney } from "@/lib/format";
import type { PriceDecision } from "@/types/api";

function statusBadge(status: PriceDecision["status"]) {
  const styles =
    status === "APPROVED"
      ? "bg-emerald-50 text-emerald-700"
      : status === "REJECTED"
        ? "bg-red-50 text-red-700"
        : "bg-amber-50 text-amber-700";
  return (
    <span className={`inline-flex rounded-full px-2 py-0.5 text-xs ${styles}`}>{status}</span>
  );
}

export function PricingDecisionsPage() {
  const { data: decisions, loading, error, reload } = useFetch(() => pricingApi.listDecisions(), []);
  const [actionId, setActionId] = useState<number | null>(null);

  async function handleAction(id: number, action: "approve" | "reject") {
    setActionId(id);
    try {
      if (action === "approve") await pricingApi.approve(id);
      else await pricingApi.reject(id);
      await reload();
    } finally {
      setActionId(null);
    }
  }

  return (
    <div className="min-h-screen">
      <Topbar title="Pricing Decisions" subtitle="Applied and suggested decisions · live API" />

      <div className="px-6 py-6 space-y-4">
        {error && (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <Card>
          <CardHeader>
            <CardTitle>Decisions</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-sm text-slate-500">Loading decisions…</div>
            ) : !decisions?.length ? (
              <div className="text-sm text-slate-500">No pricing decisions yet.</div>
            ) : (
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
                      <Th>Actions</Th>
                    </Tr>
                  </THead>
                  <TBody>
                    {decisions.map((d) => (
                      <Tr key={d.id}>
                        <Td className="font-medium">{d.productTitle}</Td>
                        <Td>{d.decisionType}</Td>
                        <Td className="text-right">{formatMoney(d.oldPrice)}</Td>
                        <Td className="text-right">{formatMoney(d.suggestedPrice)}</Td>
                        <Td>{statusBadge(d.status)}</Td>
                        <Td className="text-slate-600 max-w-xs truncate">{d.reason}</Td>
                        <Td>
                          {d.status === "SUGGESTED" ? (
                            <div className="flex gap-2">
                              <button
                                disabled={actionId === d.id}
                                onClick={() => void handleAction(d.id, "approve")}
                                className="text-xs text-emerald-700 hover:underline disabled:opacity-50"
                              >
                                Approve
                              </button>
                              <button
                                disabled={actionId === d.id}
                                onClick={() => void handleAction(d.id, "reject")}
                                className="text-xs text-red-700 hover:underline disabled:opacity-50"
                              >
                                Reject
                              </button>
                            </div>
                          ) : (
                            <span className="text-xs text-slate-400">—</span>
                          )}
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
