package tk.hiddenname.smarthome.model.timetable.objects

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.timetable.TimetableValidationGroup
import tk.hiddenname.smarthome.model.timetable.validators.TimeIntervalConstraint
import java.time.DayOfWeek
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "day_of_week_with_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "day_of_week", "time_intervals"])
class DayOfWeekWithTimeIntervals(
    @Enumerated(EnumType.STRING)
    @field:NotNull(message = "day_of_week should be specified", groups = [TimetableValidationGroup::class])
    val dayOfWeek: DayOfWeek? = null,

    @JoinTable(
        name = "day_of_week_with_time_intervals_time_interval",
        joinColumns = [JoinColumn(name = "fk_day_of_week_with_time_intervals", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "fk_time_interval", referencedColumnName = "id")]
    )
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    @field:TimeIntervalConstraint(groups = [TimetableValidationGroup::class])
    @field:NotEmpty(message = "time_intervals list can not be empty", groups = [TimetableValidationGroup::class])
    val timeIntervals: MutableList<@Valid TimeInterval> = mutableListOf()
): AbstractJpaPersistable()