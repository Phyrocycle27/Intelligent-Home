package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractJpaPersistableWithTimestamps(

        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Column(nullable = false, updatable = false)
        var creationTimestamp: LocalDateTime? = null,


        @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Column(nullable = false, updatable = true)
        var updateTimestamp: LocalDateTime? = null
) : AbstractJpaPersistable()