package tk.hiddenname.smarthome.model

import lombok.Data
import org.jetbrains.annotations.NotNull
import javax.persistence.*
import javax.validation.constraints.Size

@Data
@Entity
@Table(name = "area")
data class Area(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(updatable = false, nullable = false)
        private var id: Long = 0L,

        @NotNull
        @Column(nullable = false, length = 25)
        private var name: @Size(min = 3, max = 25) String = "",

        @Column(length = 50)
        private var description: @Size(min = 0, max = 50) String = ""
)