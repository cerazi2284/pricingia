import { cn } from "@/lib/cn";
import { Search } from "lucide-react";

export function Topbar(props: { title: string; subtitle?: string }) {
  return (
    <header className="flex items-center justify-between gap-4 border-b border-slate-200 bg-white px-6 py-4">
      <div>
        <div className="text-lg font-semibold text-slate-900">{props.title}</div>
        {props.subtitle ? <div className="text-sm text-slate-500">{props.subtitle}</div> : null}
      </div>

      <div className="hidden md:flex items-center gap-2">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
          <input
            className={cn(
              "h-10 w-[320px] rounded-lg border border-slate-200 bg-white pl-9 pr-3 text-sm",
              "placeholder:text-slate-400 focus:outline-none focus:ring-2 focus:ring-slate-900/10",
            )}
            placeholder="Search products, SKUs… (mock)"
          />
        </div>
        <div className="h-10 rounded-lg border border-slate-200 bg-slate-50 px-3 grid place-items-center text-sm text-slate-700">
          USD
        </div>
      </div>
    </header>
  );
}

