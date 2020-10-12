package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Data
@Entity
@Table(name = "sensor")
@EqualsAndHashCode(of = ["id"])
@JsonNaming(SnakeCaseStrategy::class)
@AllArgsConstructor
@NoArgsConstructor
class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private val id: Long? = null

    @Column(nullable = false, length = 25)
    private val name: @NotNull @Size(min = 3, max = 25) String? = null

    @Column(nullable = false, length = 50)
    private val description: @Size(min = 3, max = 50) String? = null

    @Column(nullable = false)
    private val reverse = false

    @Column(nullable = false)
    private val areaId = 0

    @Column(updatable = false, nullable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
    private val creationDate: LocalDateTime? = null

    @Embedded
    @AttributeOverrides(value = [AttributeOverride(name = "type", column = Column(nullable = false, updatable = false)), AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)), AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))])
    private val gpio: @NotNull GPIO? = null
}