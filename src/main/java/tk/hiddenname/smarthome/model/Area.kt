package tk.hiddenname.smarthome.model

import lombok.Data
import org.jetbrains.annotations.NotNull
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Size

@Data
@Entity
@Table(name = "area")
data class Area(@NotNull
                @Column(nullable = false, length = 25)
                var name: @Size(min = 3, max = 25) String = "",

                @Column(length = 50)
                var description: @Size(min = 0, max = 50) String = ""
) : AbstractJpaPersistable()