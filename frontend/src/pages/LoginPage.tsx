import { Link } from "react-router-dom";

export function LoginPage() {
  return (
    <div className="min-h-screen bg-slate-50 grid place-items-center px-6">
      <div className="w-full max-w-md rounded-2xl border border-slate-200 bg-white p-6">
        <div className="text-lg font-semibold text-slate-900">Login (placeholder)</div>
        <div className="mt-1 text-sm text-slate-600">
          Autenticação real via Shopify/SSO será implementada depois. Por enquanto, navegue para o dashboard.
        </div>

        <div className="mt-6 space-y-3">
          <div className="space-y-1">
            <div className="text-sm font-medium text-slate-700">Email</div>
            <input className="h-10 w-full rounded-lg border border-slate-200 px-3 text-sm" placeholder="merchant@store.com" />
          </div>
          <div className="space-y-1">
            <div className="text-sm font-medium text-slate-700">Password</div>
            <input className="h-10 w-full rounded-lg border border-slate-200 px-3 text-sm" placeholder="••••••••" type="password" />
          </div>
        </div>

        <div className="mt-6 flex items-center justify-between">
          <Link className="text-sm text-slate-600 hover:text-slate-900" to="/">
            Back to landing
          </Link>
          <Link className="rounded-lg bg-slate-900 px-4 py-2 text-sm text-white hover:bg-slate-800" to="/app/dashboard">
            Continue
          </Link>
        </div>
      </div>
    </div>
  );
}

