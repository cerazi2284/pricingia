import { useState } from "react";
import { analyticsApi, pricingApi } from "@/api";
import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
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

export function DashboardPage() {
  const { data: summary, loading: summaryLoading, error: summaryError, reload: reloadSummary } =
    useFetch(() => analyticsApi.getSummary(), []);
  const {
    data: decisions,
    loading: decisionsLoading,
    error: decisionsError,
    reload: reloadDecisions,
  } = useFetch(() => pricingApi.listDecisions(), []);
  const [actionId, setActionId] = useState<number | null>(null);

  const latest = decisions?.slice(0, 3) ?? [];
  const featured = decisions?.find((d) => d.status === "SUGGESTED") ?? decisions?.[0];

  async function handleAction(id: number, action: "approve" | "reject") {
    setActionId(id);
    try {
      if (action === "approve") await pricingApi.approve(id);
      else await pricingApi.reject(id);
      await Promise.all([reloadSummary(), reloadDecisions()]);
    } finally {
      setActionId(null);
    }
  }

  const metrics = summary
    ? [
        { label: "Products monitored", value: String(summary.productsMonitored) },
        { label: "Suggestions", value: String(summary.suggestions) },
        { label: "Approved", value: String(summary.approved) },
        { label: "Rejected", value: String(summary.rejected) },
        {
          label: "Potential uplift",
          value: formatMoney(summary.potentialUplift, summary.currency),
        },
      ]
    : [];

  return (
    <div className="min-h-screen">
      <Topbar
        title="Dashboard"
        subtitle={summary ? `${summary.shopDomain} · live API` : "Loading metrics…"}
      />

      <div className="px-6 py-6 space-y-6">
        {(summaryError || decisionsError) && (
          <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {summaryError ?? decisionsError}
          </div>
        )}

        <section className="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
          {summaryLoading
            ? Array.from({ length: 5 }).map((_, i) => (
                <Card key={i}>
                  <CardHeader>
                    <CardTitle className="text-slate-400">Loading…</CardTitle>
                  </CardHeader>
                </Card>
              ))
            : metrics.map((m) => (
                <Card key={m.label}>
                  <CardHeader>
                    <CardTitle>{m.label}</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="text-2xl font-semibold text-slate-900">{m.value}</div>
                  </CardContent>
                </Card>
              ))}
        </section>

        <section className="grid gap-4 lg:grid-cols-3">
          <Card className="lg:col-span-2">
            <CardHeader>
              <CardTitle>Latest pricing decisions</CardTitle>
            </CardHeader>
            <CardContent>
              {decisionsLoading ? (
                <div className="text-sm text-slate-500">Loading decisions…</div>
              ) : latest.length === 0 ? (
                <div className="text-sm text-slate-500">
                  No decisions yet. Load demo products and send a webhook.
                </div>
              ) : (
                <div className="space-y-3">
                  {latest.map((d) => (
                    <div key={d.id} className="rounded-xl border border-slate-200 bg-white p-3">
                      <div className="flex items-center justify-between gap-3">
                        <div className="min-w-0">
                          <div className="font-medium text-slate-900 truncate">{d.productTitle}</div>
                          <div className="text-xs text-slate-600">{d.reason}</div>
                        </div>
                        <div className="text-right">
                          <div className="text-sm font-semibold text-slate-900">
                            {formatMoney(d.oldPrice)} → {formatMoney(d.suggestedPrice)}
                          </div>
                          <div className="mt-1">{statusBadge(d.status)}</div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Top suggestion</CardTitle>
            </CardHeader>
            <CardContent>
              {featured ? (
                <>
                  <div className="text-sm text-slate-700">
                    {featured.decisionType} <span className="font-medium">{featured.productTitle}</span> to{" "}
                    <span className="font-medium">{formatMoney(featured.suggestedPrice)}</span>
                  </div>
                  <div className="mt-2 text-xs text-slate-500">{featured.reason}</div>
                  {featured.status === "SUGGESTED" && (
                    <div className="mt-4 flex gap-2">
                      <button
                        disabled={actionId === featured.id}
                        onClick={() => void handleAction(featured.id, "reject")}
                        className="h-9 flex-1 rounded-lg border border-slate-200 bg-white text-sm hover:bg-slate-50 disabled:opacity-50"
                      >
                        Reject
                      </button>
                      <button
                        disabled={actionId === featured.id}
                        onClick={() => void handleAction(featured.id, "approve")}
                        className="h-9 flex-1 rounded-lg bg-slate-900 text-white text-sm hover:bg-slate-800 disabled:opacity-50"
                      >
                        Approve
                      </button>
                    </div>
                  )}
                </>
              ) : (
                <div className="text-sm text-slate-500">No suggestions available.</div>
              )}
            </CardContent>
          </Card>
        </section>
      </div>
    </div>
  );
}
