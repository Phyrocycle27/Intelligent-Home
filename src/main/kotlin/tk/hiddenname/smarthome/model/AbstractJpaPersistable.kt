package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaPersistable {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    open var id: Long = 0L
}