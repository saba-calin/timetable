import React, { useState } from "react";
import axios from "axios";

export default function LandingPage() {
    const [group, setGroup] = useState("");
    const [timetable, setTimetable] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const getTodayDate = () => {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, "0");
        const dd = String(today.getDate()).padStart(2, "0");
        return `${yyyy}-${mm}-${dd}`;
    };

    const fetchTimetable = async () => {
        if (!group.trim()) return;
        setLoading(true);
        setError("");
        setTimetable([]);
        const today = getTodayDate();

        try {
            const res = await axios.get("http://localhost:8080/api/v1/course", {
                semiGroup: group,
                day: today,
            });
            setTimetable(res.data);
        } catch (err) {
            setError("Could not fetch timetable. Please check the group name.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-100 to-indigo-200 flex flex-col items-center justify-center px-4">
            <div className="bg-white shadow-xl rounded-2xl p-8 max-w-xl w-full">
                <h1 className="text-3xl font-bold text-center mb-6 text-indigo-800">
                    Daily Timetable
                </h1>

                <input
                    type="text"
                    placeholder="Enter your group (e.g., 217)"
                    className="w-full p-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-400 mb-4"
                    value={group}
                    onChange={(e) => setGroup(e.target.value)}
                />

                <button
                    onClick={fetchTimetable}
                    className="w-full bg-indigo-600 text-white font-semibold py-3 rounded-lg hover:bg-indigo-700 transition"
                    disabled={loading}
                >
                    {loading ? "Loading..." : "Get Today's Timetable"}
                </button>

                {error && (
                    <div className="text-red-600 text-sm mt-4 text-center">{error}</div>
                )}

                {timetable.length > 0 && (
                    <div className="mt-6">
                        <h2 className="text-xl font-semibold mb-4 text-gray-700">
                            Timetable for {group.toUpperCase()}
                        </h2>
                        <ul className="space-y-3">
                            {timetable.map((course, index) => (
                                <li
                                    key={index}
                                    className="bg-indigo-50 p-4 rounded-lg border border-indigo-200 shadow-sm"
                                >
                                    <div className="font-bold text-indigo-800">
                                        {course.course} ({course.type})
                                    </div>
                                    <div className="text-sm text-gray-700 mt-1">
                                        <strong>Hour:</strong> {course.hour}
                                    </div>
                                    <div className="text-sm text-gray-700">
                                        <strong>Room:</strong> {course.room}
                                    </div>
                                    <div className="text-sm text-gray-700">
                                        <strong>Professor:</strong> {course.professor}
                                    </div>
                                    <div className="text-xs text-gray-500 mt-1">
                                        Frequency: {course.frequency} | Formation:{" "}
                                        {course.formation}
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>
    );
}
