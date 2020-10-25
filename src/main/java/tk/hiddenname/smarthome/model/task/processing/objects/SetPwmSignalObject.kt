package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "processing_set_pwm_signal")
@JsonNaming(SnakeCaseStrategy::class)
data class SetPwmSignalObject(
        @Column(name = "device_id", nullable = false, updatable = false)
        val deviceId: Long = 0,

        @Column(nullable = false)
        val delay: Int = 0,

        @Column(name = "traget_signal", nullable = false)
        val targetSignal: Int = 0
) : ProcessingObject()