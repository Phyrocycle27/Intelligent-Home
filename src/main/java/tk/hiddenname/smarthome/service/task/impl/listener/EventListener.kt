package tk.hiddenname.smarthome.service.task.impl.listener

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.NoSuchListenerException
import tk.hiddenname.smarthome.exception.TriggerExistsException
import tk.hiddenname.smarthome.exception.TriggerNotFoundException
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.service.task.impl.TaskManager
import java.util.*

@Component
@Scope("prototype")
class EventListener(private val taskManager: TaskManager,
                    private val listenerFactory: ListenerFactory) {

    private val log = LoggerFactory.getLogger(EventListener::class.java)

    private val flags = HashMap<Long, Boolean>()
    private val listeners = HashMap<Long, Listener>()

    @Throws(TriggerExistsException::class, UnsupportedTriggerObjectTypeException::class,
            NoSuchListenerException::class)
    fun registerListeners(triggerObjects: Set<TriggerObject>) {
        triggerObjects.forEach {
            registerListener(it)
        }
    }

    @Throws(TriggerExistsException::class, UnsupportedTriggerObjectTypeException::class,
            NoSuchListenerException::class)
    fun registerListener(triggerObject: TriggerObject) {
        if (!listeners.containsKey(triggerObject.id)) {
            listeners[triggerObject.id] = listenerFactory.create(triggerObject, this)
            flags[triggerObject.id] = false
        } else {
            throw TriggerExistsException(triggerObject.id)
        }
    }

    fun unregisterListeners() {
        listeners.keys.forEach {
            try {
                unregisterListener(it)
            } catch (e: TriggerNotFoundException) {
                log.warn(e.message)
            }
        }
    }

    @Throws(TriggerNotFoundException::class)
    fun unregisterListener(id: Long) {
        listeners[id]?.unregister() ?: throw TriggerNotFoundException(id)
        listeners.remove(id)
        flags.remove(id)
    }

    @Throws(TriggerNotFoundException::class)
    fun updateFlag(id: Long, flag: Boolean) {
        if (listeners.containsKey(id)) {
            flags[id] = flag
            if (check()) {
                taskManager.processor.process()
            }
        } else {
            throw TriggerNotFoundException(id)
        }
    }

    private fun check(): Boolean {
        return !flags.containsValue(false)
    }
}