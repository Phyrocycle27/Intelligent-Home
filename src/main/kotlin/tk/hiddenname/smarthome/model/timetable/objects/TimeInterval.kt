package tk.hiddenname.smarthome.model.timetable.objects

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.timetable.TimetableValidationGroup
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "time_intervals")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["start_time", "finish_time"])
class TimeInterval(
    @field:Min(0)
    @field:Max(86400)
    @field:NotNull(message = "start time should be specified", groups = [TimetableValidationGroup::class])
    val startTime: Long? = null,

    @field:Min(0)
    @field:Max(86400)
    @field:NotNull(message = "finish time should be specified", groups = [TimetableValidationGroup::class])
    val finishTime: Long? = null
): AbstractJpaPersistable()
