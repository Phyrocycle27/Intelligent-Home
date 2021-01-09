package tk.hiddenname.smarthome.model.timetable.validators

import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [TimeIntervalValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class TimeIntervalConstraint(
    val message: String = "Time intervals overlap or start time is lower than finish time",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)