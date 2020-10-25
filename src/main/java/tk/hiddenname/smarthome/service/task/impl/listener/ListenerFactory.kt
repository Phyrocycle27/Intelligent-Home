package tk.hiddenname.smarthome.service.task.impl.listener

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.NoSuchListenerException
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.service.task.impl.listener.impl.ChangeDigitalSignalListener

@Component
class ListenerFactory(private val ctx: ApplicationContext) {

    @Throws(NoSuchListenerException::class, UnsupportedTriggerObjectTypeException::class)
    fun create(triggerObject: TriggerObject, eventListener: EventListener): Listener {
        val listener = when (triggerObject.action) {
            TriggerAction.CHANGE_DIGITAL_SIGNAL -> ctx.getBean(ChangeDigitalSignalListener::class.java, eventListener)
            else -> throw NoSuchListenerException()
        }
        listener.register(triggerObject)
        return listener
    }
}