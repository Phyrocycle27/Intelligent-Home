package tk.hiddenname.smarthome.model.timetable.validators

import tk.hiddenname.smarthome.model.timetable.objects.DayOfWeekWithTimeIntervals
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DayOfWeekWithTimeIntervalsValidator :
    ConstraintValidator<DayOfWeekWithTimeIntervalsConstraint, MutableList<DayOfWeekWithTimeIntervals>> {

    override fun isValid(
        value: MutableList<DayOfWeekWithTimeIntervals>?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (value == null) return false

        val daysOfWeek = value.map { it.dayOfWeek }

        daysOfWeek.forEachIndexed { index, dayOfWeek ->
            if (index + 1 < daysOfWeek.size && daysOfWeek.subList(index + 1, daysOfWeek.size).contains(dayOfWeek))
                return false
        }

        return true
    }
}
