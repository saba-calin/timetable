import React, { useState } from "react";
import axios from "axios";

export default function LandingPage() {
    const [group, setGroup] = useState("");
    const [timetableGroup, setTimetableGroup] = useState("");
    const [timetable, setTimetable] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [dayName, setDayName] = useState("");

    function convertDayROtoEN(dayRO) {
        const mapping = {
            Duminica: "Sunday",
            Luni: "Monday",
            Marti: "Tuesday",
            Miercuri: "Wednesday",
            Joi: "Thursday",
            Vineri: "Friday",
            Sambata: "Saturday",
        };

        return mapping[dayRO] || dayRO; // fallback returns original if not found
    }


    const getToday = () => {
        const today = new Date();

        const dayIndex = today.getDay(); // 0 = Sunday, 1 = Monday, ...
        const dayNameRO = [
            "Duminica", "Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata"
        ][dayIndex];

        return dayNameRO;
    };

    const fetchTimetable = async () => {
        if (!group.trim()) return;
        setLoading(true);
        setError("");
        setTimetable([]);

        const dayNameRO = getToday();

        try {
            const res = await axios.get(`https://timetable-latest.onrender.com/api/v1/course?semi-group=${group}&day=${dayNameRO}`);
            setTimetable(res.data);
            setDayName(convertDayROtoEN(dayNameRO));
            setTimetableGroup(group);
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

                <div className="mb-6 text-center">
                    <h2 className="text-2xl font-extrabold text-indigo-900 mb-2">
                        Tired of hunting for your daily schedule?
                    </h2>
                    <p className="text-indigo-700 text-sm max-w-md mx-auto">
                        Get your group’s timetable in just one click — no fuss, no stress.
                    </p>
                </div>

                <input
                    type="text"
                    placeholder="Enter your group (e.g., 217, 926/2)"
                    className="w-full p-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-400 mb-4"
                    value={group}
                    onChange={(e) => setGroup(e.target.value)}
                />

                <button
                    onClick={fetchTimetable}
                    className="w-full bg-indigo-600 text-white font-semibold py-3 rounded-lg hover:bg-indigo-700 transition cursor-pointer"
                    disabled={loading}
                >
                    {loading ? "Loading..." : "Get Today's Timetable"}
                </button>

                {error && (
                    <div className="text-red-600 text-sm mt-4 text-center">{error}</div>
                )}

                {timetable.length > 0 ? (
                    <div className="mt-6">
                        <h2 className="text-xl font-semibold mb-4 text-gray-700">
                            Timetable for {timetableGroup.toUpperCase()} - {dayName}
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
                                        Frequency: {course.frequency} | Formation: {course.formation}
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                ) : !loading && dayName && (
                    <div className="mt-6 text-center text-lg text-indigo-800">
                        <div className="text-4xl mb-2">🏖️</div>
                        <p>No classes for {dayName}! Enjoy your free time 😎</p>
                    </div>
                )}

            </div>
        </div>
    );
}
