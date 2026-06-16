import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Topbar } from "@/components/app/Topbar";
import { dashboardMetrics } from "@/mocks/metrics";
import { pricingDecisionsMock } from "@/mocks/pricing-decisions";

export function DashboardPage() {
  return (
    <div className="min-h-screen">
      <Topbar title="Dashboard" subtitle="Metrics overview (mock data)" />

      <div className="px-6 py-6 space-y-6">
        <section className="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
          {dashboardMetrics.map((m) => (
            <Card key={m.key}>
              <CardHeader>
                <CardTitle>{m.label}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-semibold text-slate-900">{m.value}</div>
                <div className="mt-1 text-xs text-slate-500">{m.deltaLabel}</div>
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
              <div className="space-y-3">
                {pricingDecisionsMock.slice(0, 3).map((d) => (
                  <div key={d.id} className="rounded-xl border border-slate-200 bg-white p-3">
                    <div className="flex items-center justify-between gap-3">
                      <div className="min-w-0">
                        <div className="font-medium text-slate-900 truncate">{d.productTitle}</div>
                        <div className="text-xs text-slate-600">{d.reason}</div>
                      </div>
                      <div className="text-right">
                        <div className="text-sm font-semibold text-slate-900">
                          ${d.fromPriceUsd} → ${d.toPriceUsd}
                        </div>
                        <div className="text-xs text-slate-500">{d.status}</div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>AI suggestion (placeholder)</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-sm text-slate-700">
                “Raise <span className="font-medium">Running Shorts</span> to <span className="font-medium">$54</span> to
                protect margin given velocity + low inventory.”
              </div>
              <div className="mt-3 text-xs text-slate-500">
                Not connected to any model yet. This is just a visual slot for future Spring AI / Ollama integration.
              </div>
              <div className="mt-4 flex gap-2">
                <button className="h-9 flex-1 rounded-lg border border-slate-200 bg-white text-sm hover:bg-slate-50">
                  Reject
                </button>
                <button className="h-9 flex-1 rounded-lg bg-slate-900 text-white text-sm hover:bg-slate-800">
                  Approve
                </button>
              </div>
            </CardContent>
          </Card>
        </section>
      </div>
    </div>
  );
}

