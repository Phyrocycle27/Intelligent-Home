package tk.hiddenname.smarthome.model.timetable.validators

import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [DayOfWeekValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DayOfWeekListConstraint(
    val message: String = "One day of the week repeats several times",
    val groups: Array<KClass<out Any>> = []
)
