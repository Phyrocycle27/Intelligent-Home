package tk.hiddenname.smarthome.model.timetable.validators

import tk.hiddenname.smarthome.model.timetable.objects.TimeInterval
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TimeIntervalValidator : ConstraintValidator<TimeIntervalConstraint, MutableList<TimeInterval>> {

    override fun isValid(value: MutableList<TimeInterval>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false

        value.sortBy { it.startTime }
        // check that start time is lower than finish time
        value.forEach { if (it.startTime!! >= it.finishTime!!) return false }

        for (i in 0 until value.size - 1) {
            val finishTime = value[i].finishTime!!
            val startTime = value[i + 1].startTime!!

            if (finishTime >= startTime) return false
        }

        return true
    }
}