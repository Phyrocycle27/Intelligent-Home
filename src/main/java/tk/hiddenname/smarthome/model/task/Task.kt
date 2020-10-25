package tk.hiddenname.smarthome.model.task

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "task")
@JsonNaming(SnakeCaseStrategy::class)
data class Task(
        @Column(nullable = false)
        val name: String = "",

        @Column(nullable = false, length = 50)
        val description: @Size(max = 50, min = 3) String = "",

        @JoinTable(name = "task_to_trigger_object",
                joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
        @OneToMany(cascade = [CascadeType.ALL])
        @LazyCollection(LazyCollectionOption.FALSE)
        val triggerObjects: MutableSet<TriggerObject> = HashSet(),

        @JoinTable(name = "task_to_processing_object",
                joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
        @OneToMany(cascade = [CascadeType.ALL])
        @LazyCollection(LazyCollectionOption.FALSE)
        val processingObjects: MutableSet<ProcessingObject> = HashSet()
) : AbstractJpaPersistable()