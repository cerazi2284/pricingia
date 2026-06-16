import { createBrowserRouter, Navigate } from "react-router-dom";
import { AppLayout } from "@/layouts/AppLayout";
import { DashboardPage } from "@/pages/DashboardPage";
import { LandingPage } from "@/pages/LandingPage";
import { LoginPage } from "@/pages/LoginPage";
import { NotFoundPage } from "@/pages/NotFoundPage";
import { PricingDecisionsPage } from "@/pages/PricingDecisionsPage";
import { ProductsPage } from "@/pages/ProductsPage";
import { SettingsPage } from "@/pages/SettingsPage";

export const router = createBrowserRouter([
  { path: "/", element: <LandingPage /> },
  { path: "/login", element: <LoginPage /> },
  {
    path: "/app",
    element: <AppLayout />,
    children: [
      { index: true, element: <Navigate to="/app/dashboard" replace /> },
      { path: "dashboard", element: <DashboardPage /> },
      { path: "products", element: <ProductsPage /> },
      { path: "pricing-decisions", element: <PricingDecisionsPage /> },
      { path: "settings", element: <SettingsPage /> },
    ],
  },
  { path: "*", element: <NotFoundPage /> },
]);

