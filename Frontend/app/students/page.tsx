"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import api from "@/lib/api";
import LoadingSpinner from "@/components/LoadingSpinner";

interface Student {
  id: string;
  fullName: string;
  email: string;
  phoneNumber: string;
  course: string;
  age: number;
  gender: string;
  address: string;
  registrationDate: string;
}

export default function StudentListPage() {
  const router = useRouter();
  const [allStudents, setAllStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [deletingId, setDeletingId] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    setLoading(true);
    try {
      const res = await api.get("/students");
      setAllStudents(res.data);
    } catch (error) {
      console.error("Error fetching students:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm("Are you sure you want to delete this student?")) return;
    setDeletingId(id);
    try {
      await api.delete(`/students/${id}`);
      setAllStudents(allStudents.filter((s) => s.id !== id));
    } catch (error) {
      console.error("Error deleting student:", error);
      alert("Failed to delete student.");
    } finally {
      setDeletingId(null);
    }
  };

  const filteredStudents = allStudents.filter((s) => {
    const q = searchQuery.trim().toLowerCase();
    if (!q) return true;
    return (
      s.fullName.toLowerCase().includes(q) ||
      s.email.toLowerCase().includes(q)
    );
  });

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-6xl mx-auto">
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Students</h1>
            {!loading && (
              <p className="text-sm text-gray-500 mt-1">
                {allStudents.length} total student{allStudents.length !== 1 ? "s" : ""}
              </p>
            )}
          </div>
          <Link
            href="/students/add"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 shadow-sm transition"
          >
            + Add Student
          </Link>
        </div>

        <div className="mb-6">
          <input
            type="text"
            placeholder="Search by name or email..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full border border-gray-300 rounded-lg px-4 py-2 text-gray-900 bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {loading ? (
          <LoadingSpinner text="Loading students..." />
        ) : filteredStudents.length === 0 ? (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 py-16 text-center">
            <p className="text-gray-500">
              {searchQuery
                ? `No students match "${searchQuery}".`
                : "No students found. Add your first student to get started."}
            </p>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-x-auto">
            <table className="w-full text-left">
              <thead className="bg-gray-50 border-b">
                <tr>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Name</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Email</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Course</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Age</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Phone</th>
                  <th className="px-4 py-3 text-sm font-semibold text-gray-600">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredStudents.map((student) => (
                  <tr
                    key={student.id}
                    className={`border-b last:border-0 hover:bg-gray-50 transition ${
                      deletingId === student.id ? "opacity-40" : ""
                    }`}
                  >
                    <td className="px-4 py-3 font-medium text-gray-800">{student.fullName}</td>
                    <td className="px-4 py-3 text-gray-600">{student.email}</td>
                    <td className="px-4 py-3 text-gray-600">{student.course}</td>
                    <td className="px-4 py-3 text-gray-600">{student.age}</td>
                    <td className="px-4 py-3 text-gray-600">{student.phoneNumber}</td>
                    <td className="px-4 py-3 flex gap-3">
                      <Link
                        href={`/students/${student.id}/edit`}
                        className="text-blue-600 hover:underline text-sm"
                      >
                        Edit
                      </Link>
                      <button
                        onClick={() => handleDelete(student.id)}
                        disabled={deletingId === student.id}
                        className="text-red-600 hover:underline text-sm disabled:opacity-50"
                      >
                        {deletingId === student.id ? "Deleting..." : "Delete"}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}