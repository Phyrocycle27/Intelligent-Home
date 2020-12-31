package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase

class TimetableTypeIdResolver : TypeIdResolverBase() {

    private var superType: JavaType? = null

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(o: Any) = null

    override fun idFromValueAndType(o: Any, aClass: Class<*>?) = null

    override fun getMechanism() = null

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val subType = when (TimetableMode.getTimetableMode(id)) {
            TimetableMode.ALWAYS -> Timetable::class.java
            TimetableMode.BY_DAYS_OF_WEEK -> TimetableByDaysOfWeek::class.java
            TimetableMode.BY_TIME_INTERVALS -> TimetableByTimeIntervals::class.java
            TimetableMode.BY_DAYS_OF_WEEK_AND_TIME_INTERVALS -> TimetableByDaysOfWeekAndTimeIntervals::class.java
        }

        return context.constructSpecializedType(superType, subType)
    }
}