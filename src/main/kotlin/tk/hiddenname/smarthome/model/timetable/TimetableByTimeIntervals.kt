package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.timetable.objects.TimeInterval
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "timetable_by_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "mode", "time_intervals"])
class TimetableByTimeIntervals(
    timetableMode: TimetableMode? = null,

    @field:NotEmpty(message = "time intervals list can not be empty", groups = [TimetableValidationGroup::class])
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    val timeIntervals: MutableList<@Valid TimeInterval> = mutableListOf()
) : Timetable(timetableMode)