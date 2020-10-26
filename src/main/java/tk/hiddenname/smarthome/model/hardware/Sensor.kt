package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "sensor")
@JsonNaming(SnakeCaseStrategy::class)
class Sensor : Hardware() {
    init {
        gpio?.pinMode = GPIOMode.OUTPUT
    }
}