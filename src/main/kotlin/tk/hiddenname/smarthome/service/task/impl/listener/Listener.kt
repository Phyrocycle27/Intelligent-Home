package tk.hiddenname.smarthome.service.task.impl.listener

import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import javax.validation.constraints.NotNull

interface Listener {

    fun register(triggerObject: @NotNull TriggerObject)

    fun update(triggerObject: @NotNull TriggerObject)

    fun unregister()

    fun trigger(flag: @NotNull Boolean)
}