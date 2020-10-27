package tk.hiddenname.smarthome.model

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "area")
@JsonPropertyOrder(value = ["id", "name", "description"])
data class Area(
        @field:NotBlank(message = "Blank")
        @field:Size(min = 3, max = 25)
        @Column(nullable = false, length = 25)
        var name: String = "",

        @field:Size(max = 50)
        @Column(nullable = false, length = 50)
        var description: String = ""
) : AbstractJpaPersistable()