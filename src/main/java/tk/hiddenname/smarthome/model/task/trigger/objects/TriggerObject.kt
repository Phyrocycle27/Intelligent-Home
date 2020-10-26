package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import tk.hiddenname.smarthome.model.AbstractJpaPersistable
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction
import javax.persistence.*

@Entity
@Table(name = "trigger_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(TriggerObjectTypeIdResolver::class)
abstract class TriggerObject(
        @Column(updatable = false, nullable = false)
        @Enumerated(EnumType.STRING)
        open val action: TriggerAction = TriggerAction.NOT_SPECIFIED
) : AbstractJpaPersistable()