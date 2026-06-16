import { cn } from "@/lib/cn";
import { BarChart3, Boxes, Settings, Sparkles, Tag, Zap } from "lucide-react";
import { NavLink } from "react-router-dom";

const navItems = [
  { to: "/app/dashboard", label: "Dashboard", icon: BarChart3 },
  { to: "/app/products", label: "Products", icon: Boxes },
  { to: "/app/pricing-decisions", label: "Pricing Decisions", icon: Tag },
  { to: "/app/settings", label: "Settings", icon: Settings },
] as const;

export function Sidebar() {
  return (
    <aside className="w-64 shrink-0 border-r border-slate-200 bg-white">
      <div className="px-4 py-4">
        <div className="flex items-center gap-2">
          <div className="h-9 w-9 rounded-lg bg-slate-900 text-white grid place-items-center">
            <Zap className="h-5 w-5" />
          </div>
          <div>
            <div className="font-semibold leading-tight">Pricing IA</div>
            <div className="text-xs text-slate-500">Shopify merchants</div>
          </div>
        </div>
      </div>

      <nav className="px-2 pb-4">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              cn(
                "flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-slate-700 hover:bg-slate-100",
                isActive && "bg-slate-900 text-white hover:bg-slate-900",
              )
            }
          >
            <item.icon className="h-4 w-4" />
            {item.label}
          </NavLink>
        ))}

        <div className="mt-4 rounded-xl border border-slate-200 bg-slate-50 p-3">
          <div className="flex items-center gap-2 text-xs font-medium text-slate-700">
            <Sparkles className="h-4 w-4" />
            AI suggestion (placeholder)
          </div>
          <div className="mt-2 text-xs text-slate-600">
            “Consider raising <span className="font-medium">Running Shorts</span> from <span className="font-medium">$49</span>{" "}
            to <span className="font-medium">$54</span> due to velocity + low stock.”
          </div>
        </div>
      </nav>
    </aside>
  );
}

