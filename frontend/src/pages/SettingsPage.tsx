import { Topbar } from "@/components/app/Topbar";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export function SettingsPage() {
  return (
    <div className="min-h-screen">
      <Topbar title="Settings" subtitle="Configuration (placeholder)" />

      <div className="px-6 py-6">
        <Card>
          <CardHeader>
            <CardTitle>Merchant settings (mock)</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-sm text-slate-700">
              Aqui vão as configurações de margem mínima/máxima, automações e guardrails.
            </div>
            <div className="mt-2 text-xs text-slate-500">
              Sem integração com backend nesta etapa.
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

