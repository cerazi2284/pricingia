import { Outlet } from "react-router-dom";
import { Sidebar } from "@/components/app/Sidebar";

export function AppLayout() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-900 flex">
      <Sidebar />
      <main className="flex-1 min-w-0">
        <Outlet />
      </main>
    </div>
  );
}

