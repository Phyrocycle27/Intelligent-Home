package tk.hiddenname.smarthome.model.hardware

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "sensor")
@JsonNaming(SnakeCaseStrategy::class)
data class Sensor(@Id
                  @GeneratedValue(strategy = GenerationType.SEQUENCE)
                  @Column(updatable = false, nullable = false)
                  private var id: Long = 0L,

                  @NotNull
                  @Column(nullable = false, length = 25)
                  private var name: @Size(min = 3, max = 25) String = "",

                  @Column(nullable = false, length = 50)
                  private var description: @Size(min = 3, max = 50) String = "",

                  @Column(nullable = false)
                  private var reverse: Boolean = false,

                  @Column(nullable = false)
                  private var areaId: Long = 0L,

                  @NotNull
                  @Column(updatable = false, nullable = false, name = "creation_date")
                  @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
                  private var creationDate: LocalDateTime? = null,

                  @NotNull
                  @Embedded
                  @AttributeOverrides(value = [
                      AttributeOverride(name = "type", column = Column(nullable = false, updatable = false)),
                      AttributeOverride(name = "gpioPin", column = Column(nullable = false, updatable = false)),
                      AttributeOverride(name = "mode", column = Column(nullable = false, updatable = false))
                  ])
                  private var gpio: GPIO? = null
)