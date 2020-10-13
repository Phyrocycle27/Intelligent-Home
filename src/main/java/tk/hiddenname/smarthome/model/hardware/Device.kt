package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.jetbrains.annotations.NotNull
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "device")
@JsonNaming(SnakeCaseStrategy::class)
data class Device(
        @NotNull
        @Column(nullable = false, length = 25)
        var name: @Size(min = 3, max = 25) String = "",

        @Column(length = 50)
        var description: @Size(min = 3, max = 50) String = "",

        @Column(nullable = false)
        var reverse: Boolean = false,

        @Column(nullable = false)
        var areaId: Long = 0L,

        @NotNull
        @Column(nullable = false, updatable = false, name = "creation_date")
        @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
        var creationDate: LocalDateTime? = null,

        @NotNull
        @Embedded
        @AttributeOverrides(value = [
                AttributeOverride(name = "type", column = Column(nullable = false, updatable = false)),
                AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)),
                AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))
        ])
        var gpio: GPIO? = null
) : AbstractJpaPersistable()