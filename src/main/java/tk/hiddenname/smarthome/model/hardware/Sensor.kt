package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "sensor")
@JsonNaming(SnakeCaseStrategy::class)
class Sensor(id: Long, name: String, description: String,
             signalInversion: Boolean, areaId: Long, creationTimestamp: LocalDateTime,
             updateTimestamp: LocalDateTime, gpio: GPIO) :
        Hardware(id, name, description, signalInversion, areaId, creationTimestamp, updateTimestamp, gpio) {

    constructor()
}