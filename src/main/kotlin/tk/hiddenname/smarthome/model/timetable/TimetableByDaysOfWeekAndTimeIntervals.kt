package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.timetable.objects.DayOfWeekWithTimeIntervals
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "timetable_by_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "mode", "days_of_week_with_time_intervals"])
class TimetableByDaysOfWeekAndTimeIntervals(
    timetableMode: TimetableMode? = null,

    @field:NotEmpty(message = "time intervals list can not be empty", groups = [TimetableValidationGroup::class])
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    val daysOfWeekWithTimeIntervals: MutableList<@Valid DayOfWeekWithTimeIntervals> = mutableListOf()
) : Timetable(timetableMode)