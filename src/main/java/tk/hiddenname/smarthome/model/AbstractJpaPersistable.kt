package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaPersistable {

    @Id
    @Column(updatable = false, nullable = false)
    @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}