package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import tk.hiddenname.smarthome.model.task.TaskValidationGroup
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(name = "processing_set_pwm_signal")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "action", "device_id", "delay", "target_signal"])
data class SetPwmSignalObject(
        @field:Min(1, groups = [TaskValidationGroup::class])
        @field:NotNull(message = "device id should be specified", groups = [TaskValidationGroup::class])
        @Column(name = "device_id", nullable = false, updatable = false)
        val deviceId: Long? = null,

        @field:Min(0, groups = [TaskValidationGroup::class])
        @field:Max(86400, groups = [TaskValidationGroup::class])
        @Column(nullable = false)
        val delay: Int = 0,

        @field:NotNull(message = "target pwm signal should be specified", groups = [TaskValidationGroup::class])
        @Column(name = "target_signal", nullable = false)
        val targetSignal: Int? = null
) : ProcessingObject() {
    override fun toString(): String {
        return "SetPwmSignalObject(deviceId=$deviceId, delay=$delay, targetSignal=$targetSignal)"
    }
}