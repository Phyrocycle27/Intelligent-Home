package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.timetable.objects.DayOfWeekWithTimeIntervals
import tk.hiddenname.smarthome.model.timetable.validators.DayOfWeekWithTimeIntervalsConstraint
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "timetable_by_days_of_week_with_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "mode", "days_of_week_with_time_intervals"])
class TimetableByDaysOfWeekAndTimeIntervals(
    @JoinTable(
        name = "timetable_by_days_with_time_to_day_of_week_with_time_intervals",
        joinColumns = [JoinColumn(name = "fk_timetable", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "fk_day_of_week_with_time_intervals", referencedColumnName = "id")]
    )
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    @field:DayOfWeekWithTimeIntervalsConstraint(groups = [TimetableValidationGroup::class])
    @field:NotEmpty(message = "days_of_week_with_time_intervals list can not be empty", groups = [TimetableValidationGroup::class])
    val daysOfWeekWithTimeIntervals: MutableList<@Valid DayOfWeekWithTimeIntervals> = mutableListOf()
) : Timetable()