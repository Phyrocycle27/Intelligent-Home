package tk.hiddenname.smarthome.service.task.impl.listener

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.exist.TriggerExistsException
import tk.hiddenname.smarthome.exception.not_found.TriggerNotFoundException
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.service.task.impl.TaskManager

@Component
@Scope("prototype")
class EventListener(
    private val taskManager: TaskManager,
    private val listenerFactory: ListenerFactory
) {

    private val log = LoggerFactory.getLogger(EventListener::class.java)

    private val flags = HashMap<Long, Boolean>()
    private val listeners = HashMap<Long, Listener>()

    fun registerListeners(triggerObjects: List<TriggerObject?>) {
        triggerObjects.forEach {
            if (it != null) {
                registerListener(it)
            }
        }
    }

    fun registerListener(triggerObject: TriggerObject) {
        if (listeners.containsKey(triggerObject.id)) {
            throw TriggerExistsException(triggerObject.id)
        } else {
            registerListenerIfNotExist(triggerObject)
        }
    }

    private fun registerListenerIfNotExist(triggerObject: TriggerObject) {
        try {
            listeners[triggerObject.id] = listenerFactory.create(triggerObject, this)
            flags[triggerObject.id] = false
        } catch (ex: Exception) {
            taskManager.unregister()
            log.error(ex.message)
            throw ex
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


    fun unregisterListener(id: Long) {
        listeners[id]?.unregister() ?: throw TriggerNotFoundException(id)
        listeners.remove(id)
        flags.remove(id)
    }


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

    fun listenersCount() = listeners.size

    private fun check(): Boolean {
        return !flags.containsValue(false)
    }
}