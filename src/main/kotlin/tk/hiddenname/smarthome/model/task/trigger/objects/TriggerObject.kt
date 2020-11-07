package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "trigger_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(TriggerObjectTypeIdResolver::class)
abstract class TriggerObject(
        @Enumerated(EnumType.STRING)
        @Column(updatable = false, nullable = false)
        open val action: TriggerAction? = null
) : AbstractJpaPersistable()