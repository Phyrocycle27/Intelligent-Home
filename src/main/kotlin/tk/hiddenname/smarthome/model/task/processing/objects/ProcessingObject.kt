package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction
import javax.persistence.*

@Entity
@Table(name = "processing_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(ProcessingObjectTypeIdResolver::class)
abstract class ProcessingObject(
        @Enumerated(EnumType.STRING)
        @Column(nullable = false, updatable = false)
        open val action: ProcessingAction? = null
) : AbstractJpaPersistable()