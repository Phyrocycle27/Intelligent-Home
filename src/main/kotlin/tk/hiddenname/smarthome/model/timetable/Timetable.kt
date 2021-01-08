package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import javax.persistence.*

@Entity
@Table(name = "timetable")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "mode",
    visible = true
)
@JsonTypeIdResolver(TimetableTypeIdResolver::class)
@JsonPropertyOrder(value = ["id", "mode"])
open class Timetable(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    open val mode: TimetableMode? = null
) : AbstractJpaPersistable()