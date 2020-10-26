package tk.hiddenname.smarthome.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(name = "area")
data class Area(
        @Column(nullable = false, length = 25)
        val name: @Size(min = 3, max = 25) String = "",

        @Column(length = 50)
        val description: @Size(min = 0, max = 50) String = ""
) : AbstractJpaPersistable()