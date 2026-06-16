import { ArrowRight, BarChart3, ShieldCheck, Sparkles } from "lucide-react";
import { Link } from "react-router-dom";

export function LandingPage() {
  return (
    <div className="min-h-screen bg-white">
      <header className="mx-auto max-w-6xl px-6 py-6 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="h-10 w-10 rounded-xl bg-slate-900 text-white grid place-items-center">
            <Sparkles className="h-5 w-5" />
          </div>
          <div className="leading-tight">
            <div className="font-semibold">Pricing IA</div>
            <div className="text-xs text-slate-500">Dynamic pricing for Shopify merchants</div>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <Link className="text-sm text-slate-700 hover:text-slate-900" to="/login">
            Login
          </Link>
          <Link
            className="inline-flex items-center gap-2 rounded-lg bg-slate-900 px-4 py-2 text-sm text-white hover:bg-slate-800"
            to="/login"
          >
            Get started <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </header>

      <main className="mx-auto max-w-6xl px-6 pb-20">
        <section className="pt-12 pb-12">
          <div className="max-w-3xl">
            <h1 className="text-4xl font-semibold tracking-tight text-slate-900">
              Protect margin, act fast, and price smarter — in real time.
            </h1>
            <p className="mt-4 text-lg text-slate-600">
              Pricing IA helps Shopify merchants make confident pricing decisions when velocity spikes, inventory drops,
              or margin opportunities appear.
            </p>
            <div className="mt-6 flex items-center gap-3">
              <Link
                to="/app/dashboard"
                className="inline-flex items-center gap-2 rounded-lg bg-slate-900 px-4 py-2 text-sm text-white hover:bg-slate-800"
              >
                View dashboard <ArrowRight className="h-4 w-4" />
              </Link>
              <div className="text-sm text-slate-500">Mock UI only (no backend integration yet)</div>
            </div>
          </div>
        </section>

        <section className="grid gap-4 md:grid-cols-3">
          <div className="rounded-xl border border-slate-200 bg-white p-5">
            <div className="flex items-center gap-2 text-sm font-medium text-slate-900">
              <BarChart3 className="h-4 w-4" />
              Revenue protection
            </div>
            <p className="mt-2 text-sm text-slate-600">
              Track price changes, margin uplift, and products under monitoring — all in USD.
            </p>
          </div>
          <div className="rounded-xl border border-slate-200 bg-white p-5">
            <div className="flex items-center gap-2 text-sm font-medium text-slate-900">
              <ShieldCheck className="h-4 w-4" />
              Guardrails
            </div>
            <p className="mt-2 text-sm text-slate-600">
              Apply floor/ceiling rules per merchant to avoid unsafe swings and protect brand perception.
            </p>
          </div>
          <div className="rounded-xl border border-slate-200 bg-white p-5">
            <div className="flex items-center gap-2 text-sm font-medium text-slate-900">
              <Sparkles className="h-4 w-4" />
              AI suggestions (placeholder)
            </div>
            <p className="mt-2 text-sm text-slate-600">
              Visual space for “AI suggestion” — no real AI integrated in this phase.
            </p>
          </div>
        </section>
      </main>
    </div>
  );
}

