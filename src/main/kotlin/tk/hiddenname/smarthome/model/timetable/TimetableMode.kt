@file:Suppress("unused")

package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonCreator
import tk.hiddenname.smarthome.exception.invalid.InvalidTimetableModeException

enum class TimetableMode {
    ALWAYS,
    BY_DAYS_OF_WEEK,
    BY_TIME_INTERVALS,
    BY_DAYS_OF_WEEK_AND_TIME_INTERVALS;

    companion object {
        @JvmStatic
        @JsonCreator
        fun getTimetableMode(timetableModeName: String): TimetableMode {
            try {
                return valueOf(timetableModeName.toUpperCase())
            } catch (e: IllegalArgumentException) {
                throw InvalidTimetableModeException(timetableModeName, values())
            }
        }
    }
}