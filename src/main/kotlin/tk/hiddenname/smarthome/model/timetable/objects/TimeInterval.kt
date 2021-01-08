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
@Table(name = "time_interval")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "start_time", "finish_time"])
class TimeInterval(
    @field:Min(0, groups = [TimetableValidationGroup::class])
    @field:Max(86400, groups = [TimetableValidationGroup::class])
    @field:NotNull(message = "start_time should be specified", groups = [TimetableValidationGroup::class])
    var startTime: Long? = null,

    @field:Min(0, groups = [TimetableValidationGroup::class])
    @field:Max(86400, groups = [TimetableValidationGroup::class])
    @field:NotNull(message = "finish_time should be specified", groups = [TimetableValidationGroup::class])
    var finishTime: Long? = null
) : AbstractJpaPersistable()
