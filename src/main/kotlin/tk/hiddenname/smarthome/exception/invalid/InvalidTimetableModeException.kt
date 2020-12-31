package tk.hiddenname.smarthome.exception.invalid

import tk.hiddenname.smarthome.exception.ApiException
import tk.hiddenname.smarthome.model.timetable.TimetableMode

class InvalidTimetableModeException(timetableModeName: String, availableTypes: Array<TimetableMode>) :
    ApiException(
        "The timetable mode '$timetableModeName' is invalid! " +
                "Available types are: ${availableTypes.joinToString(", ")}"
    )