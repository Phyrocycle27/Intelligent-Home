package tk.hiddenname.smarthome.service.task.impl.listener

import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import javax.validation.constraints.NotNull

interface Listener {

    @Throws(UnsupportedTriggerObjectTypeException::class)
    fun register(triggerObject: @NotNull TriggerObject)

    @Throws(UnsupportedTriggerObjectTypeException::class)
    fun update(triggerObject: @NotNull TriggerObject)

    fun unregister()

    fun trigger(flag: @NotNull Boolean)
}