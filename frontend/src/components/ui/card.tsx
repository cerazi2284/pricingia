import { cn } from "@/lib/cn";
import type { ReactNode } from "react";

export function Card(props: { className?: string; children: ReactNode }) {
  return <div className={cn("rounded-xl border border-slate-200 bg-white", props.className)}>{props.children}</div>;
}

export function CardHeader(props: { className?: string; children: ReactNode }) {
  return <div className={cn("px-4 py-3 border-b border-slate-100", props.className)}>{props.children}</div>;
}

export function CardTitle(props: { className?: string; children: ReactNode }) {
  return <div className={cn("text-sm font-medium text-slate-600", props.className)}>{props.children}</div>;
}

export function CardContent(props: { className?: string; children: ReactNode }) {
  return <div className={cn("px-4 py-4", props.className)}>{props.children}</div>;
}

