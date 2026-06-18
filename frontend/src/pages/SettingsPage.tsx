import { FormEvent, useEffect, useState } from "react";
import { merchantApi } from "@/api";
import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useFetch } from "@/hooks/useFetch";

export function SettingsPage() {
  const { data: settings, loading, error, reload } = useFetch(() => merchantApi.getSettings(), []);
  const [currency, setCurrency] = useState("USD");
  const [minMargin, setMinMargin] = useState("30");
  const [maxIncrease, setMaxIncrease] = useState("15");
  const [automation, setAutomation] = useState(true);
  const [saving, setSaving] = useState(false);
  const [saveMessage, setSaveMessage] = useState<string | null>(null);

  useEffect(() => {
    if (!settings) return;
    setCurrency(settings.currency);
    setMinMargin(String(settings.minMarginPercentage));
    setMaxIncrease(String(settings.maxPriceIncreasePercentage));
    setAutomation(settings.automationEnabled);
  }, [settings]);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setSaving(true);
    setSaveMessage(null);
    try {
      await merchantApi.updateSettings({
        currency,
        minMarginPercentage: Number(minMargin),
        maxPriceIncreasePercentage: Number(maxIncrease),
        automationEnabled: automation,
      });
      setSaveMessage("Settings saved.");
      await reload();
    } catch (err) {
      setSaveMessage(err instanceof Error ? err.message : "Failed to save");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen">
      <Topbar
        title="Settings"
        subtitle={settings ? `${settings.shopDomain} · live API` : "Merchant configuration"}
      />

      <div className="px-6 py-6">
        {(error || saveMessage) && (
          <div
            className={`mb-4 rounded-xl border px-4 py-3 text-sm ${
              error || saveMessage?.includes("Failed")
                ? "border-red-200 bg-red-50 text-red-700"
                : "border-emerald-200 bg-emerald-50 text-emerald-700"
            }`}
          >
            {error ?? saveMessage}
          </div>
        )}

        <Card>
          <CardHeader>
            <CardTitle>Merchant settings</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="text-sm text-slate-500">Loading settings…</div>
            ) : (
              <form onSubmit={(e) => void handleSubmit(e)} className="max-w-lg space-y-4">
                <label className="block space-y-1">
                  <span className="text-sm text-slate-700">Currency</span>
                  <input
                    value={currency}
                    onChange={(e) => setCurrency(e.target.value)}
                    className="h-10 w-full rounded-lg border border-slate-200 px-3 text-sm"
                  />
                </label>

                <label className="block space-y-1">
                  <span className="text-sm text-slate-700">Minimum margin (%)</span>
                  <input
                    type="number"
                    min={0}
                    max={100}
                    step="0.01"
                    value={minMargin}
                    onChange={(e) => setMinMargin(e.target.value)}
                    className="h-10 w-full rounded-lg border border-slate-200 px-3 text-sm"
                  />
                </label>

                <label className="block space-y-1">
                  <span className="text-sm text-slate-700">Max price increase (%)</span>
                  <input
                    type="number"
                    min={0}
                    max={100}
                    step="0.01"
                    value={maxIncrease}
                    onChange={(e) => setMaxIncrease(e.target.value)}
                    className="h-10 w-full rounded-lg border border-slate-200 px-3 text-sm"
                  />
                </label>

                <label className="flex items-center gap-2 text-sm text-slate-700">
                  <input
                    type="checkbox"
                    checked={automation}
                    onChange={(e) => setAutomation(e.target.checked)}
                  />
                  Automation enabled
                </label>

                <button
                  type="submit"
                  disabled={saving}
                  className="h-10 rounded-lg bg-slate-900 px-4 text-sm text-white hover:bg-slate-800 disabled:opacity-50"
                >
                  {saving ? "Saving…" : "Save settings"}
                </button>
              </form>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
