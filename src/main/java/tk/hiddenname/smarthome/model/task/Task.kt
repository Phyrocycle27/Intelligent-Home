package tk.hiddenname.smarthome.model.task

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "task")
@JsonNaming(SnakeCaseStrategy::class)
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false, length = 50)
    var description: @Size(min = 3, max = 50) String? = null

    @JoinTable(name = "task_to_trigger_object",
            joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var triggerObjects: @NotNull MutableSet<TriggerObject>? = HashSet()

    @JoinTable(name = "task_to_processing_object",
            joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    var processingObjects: @NotNull MutableSet<ProcessingObject>? = HashSet()
}