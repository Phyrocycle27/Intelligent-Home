package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "processing_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(ProcessingObjectTypeIdResolver::class)
abstract class ProcessingObject(
        @Enumerated(EnumType.STRING)
        @Column(nullable = false, updatable = false)
        @field:NotNull(message = "processing action field can not be null")
        open val action: ProcessingAction? = null,

        @field:Size(min = 3, max = 25)
        @field:NotBlank(message = "name shouldn't be empty or null ")
        @Column(nullable = false, length = 25)
        open var name: String? = null,
) : AbstractJpaPersistable()