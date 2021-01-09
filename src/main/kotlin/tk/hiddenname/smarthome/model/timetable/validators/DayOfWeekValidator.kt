package tk.hiddenname.smarthome.model.timetable.validators

import java.time.DayOfWeek
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DayOfWeekValidator : ConstraintValidator<DayOfWeekListConstraint, MutableList<DayOfWeek>> {

    override fun isValid(value: MutableList<DayOfWeek>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false

        value.forEachIndexed { index, dayOfWeek ->
            if (index + 1 < value.size && value.subList(index + 1, value.size).contains(dayOfWeek)) return false
        }

        return true
    }
}