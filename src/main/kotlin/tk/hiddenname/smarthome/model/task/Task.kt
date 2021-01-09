package tk.hiddenname.smarthome.model.task

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import tk.hiddenname.smarthome.model.AbstractJpaPersistableWithTimestamps
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.model.timetable.Timetable
import tk.hiddenname.smarthome.model.timetable.TimetableValidationGroup
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "task")
@JsonNaming(SnakeCaseStrategy::class)
@JsonPropertyOrder(
    value = ["id", "name", "description", "creationTimestamp",
        "updateTimestamp", "timetable", "triggerObjects", "processingObjects"]
)
class Task(
    @field:NotBlank(message = "name field shouldn't be empty or null", groups = [TaskValidationGroup::class])
    @field:Size(min = 3, max = 25, groups = [TaskValidationGroup::class])
    @Column(nullable = false)
    val name: String = "",

    @field:Size(max = 50, groups = [TaskValidationGroup::class])
    @Column(nullable = false, length = 50)
    val description: String = "",

    @JoinTable(
        name = "task_to_trigger_object",
        joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")]
    )
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    @field:NotEmpty(message = "trigger_objects list can not be empty", groups = [TaskValidationGroup::class])
    val triggerObjects: MutableList<@Valid TriggerObject> = mutableListOf(),

    @JoinTable(
        name = "task_to_processing_object",
        joinColumns = [JoinColumn(name = "fk_task", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "fk_object", referencedColumnName = "id")]
    )
    @OneToMany(cascade = [CascadeType.ALL])
    @LazyCollection(LazyCollectionOption.FALSE)
    @field:NotEmpty(message = "processing_objects list can not be empty", groups = [TaskValidationGroup::class])
    val processingObjects: MutableList<@Valid ProcessingObject> = mutableListOf(),

    @field:Valid
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "timetable_id", referencedColumnName = "id")
    @field:NotNull(message = "timetable_object shouldn't be null", groups = [TimetableValidationGroup::class])
    val timetable: Timetable? = null

) : AbstractJpaPersistableWithTimestamps() {

    override fun toString(): String {
        return "Task(name='$name', description='$description', " +
                "triggerObjects=$triggerObjects, processingObjects=$processingObjects)"
    }
}