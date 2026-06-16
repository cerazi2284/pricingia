import { Link } from "react-router-dom";

export function NotFoundPage() {
  return (
    <div className="min-h-screen bg-slate-50 grid place-items-center px-6">
      <div className="max-w-md rounded-2xl border border-slate-200 bg-white p-6 text-center">
        <div className="text-2xl font-semibold text-slate-900">404</div>
        <div className="mt-2 text-sm text-slate-600">Page not found</div>
        <div className="mt-6 flex items-center justify-center gap-2">
          <Link className="rounded-lg bg-slate-900 px-4 py-2 text-sm text-white hover:bg-slate-800" to="/">
            Go to landing
          </Link>
          <Link className="rounded-lg border border-slate-200 px-4 py-2 text-sm hover:bg-slate-50" to="/app/dashboard">
            Dashboard
          </Link>
        </div>
      </div>
    </div>
  );
}

