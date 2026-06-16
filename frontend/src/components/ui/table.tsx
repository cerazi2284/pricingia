import { cn } from "@/lib/cn";
import type { ReactNode } from "react";

export function Table(props: { className?: string; children: ReactNode }) {
  return <table className={cn("w-full text-sm", props.className)}>{props.children}</table>;
}

export function THead(props: { children: ReactNode }) {
  return <thead className="bg-slate-50 text-slate-600">{props.children}</thead>;
}

export function TBody(props: { children: ReactNode }) {
  return <tbody className="divide-y divide-slate-100">{props.children}</tbody>;
}

export function Tr(props: { className?: string; children: ReactNode }) {
  return <tr className={cn("hover:bg-slate-50/60", props.className)}>{props.children}</tr>;
}

export function Th(props: { className?: string; children: ReactNode }) {
  return <th className={cn("text-left font-medium px-3 py-2", props.className)}>{props.children}</th>;
}

export function Td(props: { className?: string; children: ReactNode }) {
  return <td className={cn("px-3 py-2 text-slate-800", props.className)}>{props.children}</td>;
}

