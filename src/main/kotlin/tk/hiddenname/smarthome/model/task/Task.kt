package tk.hiddenname.smarthome.model.task

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.AbstractJpaPersistableWithTimestamps
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "task")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "name", "description", "creationTimestamp",
        "updateTimestamp", "triggerObjects", "processingObjects"])
class Task(
        @field:Size(min = 3, max = 25, groups = [TaskValidationGroup::class])
        @field:NotBlank(message = "name shouldn't be empty or null", groups = [TaskValidationGroup::class])
        @Column(nullable = false)
        val name: String = "",

        @field:Size(max = 50, groups = [TaskValidationGroup::class])
        @Column(nullable = false, length = 50)
        val description: String = "",

        @JoinTable(name = "task_to_trigger_object",
                joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
        @OneToMany(cascade = [CascadeType.ALL])
        @LazyCollection(LazyCollectionOption.FALSE)
        @field:NotEmpty(message = "trigger objects list can not be empty", groups = [TaskValidationGroup::class])
        val triggerObjects: MutableList<@Valid TriggerObject> = mutableListOf(),

        @JoinTable(name = "task_to_processing_object",
                joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")])
        @OneToMany(cascade = [CascadeType.ALL])
        @LazyCollection(LazyCollectionOption.FALSE)
        @field:NotEmpty(message = "processing objects list can not be empty", groups = [TaskValidationGroup::class])
        val processingObjects: MutableList<@Valid ProcessingObject> = mutableListOf(),
) : AbstractJpaPersistableWithTimestamps() {

    override fun toString(): String {
        return "Task(name='$name', description='$description', " +
                "triggerObjects=$triggerObjects, processingObjects=$processingObjects)"
    }
}