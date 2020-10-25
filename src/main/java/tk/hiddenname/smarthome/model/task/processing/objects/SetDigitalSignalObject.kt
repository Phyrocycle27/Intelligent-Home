package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "processing_set_digital_signal")
@JsonNaming(SnakeCaseStrategy::class)
data class SetDigitalSignalObject(
        @Column(name = "device_id", nullable = false, updatable = false)
        val deviceId: Long = 0,

        @Column(nullable = false)
        val delay: Int = 0,

        @Column(name = "target_state", nullable = false)
        val targetState: Boolean = false
) : ProcessingObject()