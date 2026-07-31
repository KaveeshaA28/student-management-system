"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";

export default function Navbar() {
  const pathname = usePathname();
  const router = useRouter();

  // Don't show navbar on the login page
  if (pathname === "/login") return null;

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("adminEmail");
    router.push("/login");
  };

  const linkClass = (path: string) =>
    `text-sm font-medium transition ${
      pathname === path
        ? "text-blue-600"
        : "text-gray-600 hover:text-gray-900"
    }`;

  return (
    <nav className="bg-white border-b shadow-sm">
      <div className="max-w-6xl mx-auto px-8 py-4 flex justify-between items-center">
        <div className="flex items-center gap-8">
          <span className="font-bold text-gray-800">Student MS</span>
          <Link href="/dashboard" className={linkClass("/dashboard")}>
            Dashboard
          </Link>
          <Link href="/students" className={linkClass("/students")}>
            Students
          </Link>
        </div>
        <button
          onClick={handleLogout}
          className="text-sm text-red-600 hover:underline"
        >
          Logout
        </button>
      </div>
    </nav>
  );
}