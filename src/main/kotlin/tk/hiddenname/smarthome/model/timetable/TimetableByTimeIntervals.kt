package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.timetable.objects.TimeInterval
import tk.hiddenname.smarthome.model.timetable.validators.TimeIntervalConstraint
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "timetable_by_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "mode", "time_intervals"])
class TimetableByTimeIntervals(
    @JoinTable(
        name = "timetable_by_time_intervals_time_intervals",
        joinColumns = [JoinColumn(name = "fk_timetable", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "fk_time_interval", referencedColumnName = "id")]
    )
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    @field:TimeIntervalConstraint(groups = [TimetableValidationGroup::class])
    @field:NotEmpty(message = "time_intervals list can not be empty", groups = [TimetableValidationGroup::class])
    val timeIntervals: MutableList<@Valid TimeInterval> = mutableListOf()
) : Timetable()