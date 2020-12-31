package tk.hiddenname.smarthome.model.timetable.objects

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.timetable.TimetableValidationGroup
import java.time.DayOfWeek
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "day_of_week_with_time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["day_of_week", "time_intervals"])
class DayOfWeekWithTimeIntervals(
    @field:NotNull(message = "day of week should be specified", groups = [TimetableValidationGroup::class])
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek? = null,

    @field:NotEmpty(message = "time intervals list can not be empty", groups = [TimetableValidationGroup::class])
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    val timeIntervals: MutableList<@Valid TimeInterval> = mutableListOf()
): AbstractJpaPersistable()