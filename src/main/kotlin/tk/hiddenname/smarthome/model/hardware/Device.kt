package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "device")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(
    value = ["id", "area_id", "name", "description", "creationTimestamp", "updateTimestamp",
        "signalInversion"]
)
class Device : Hardware()