package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "device")
@JsonNaming(SnakeCaseStrategy::class)
class Device : Hardware()