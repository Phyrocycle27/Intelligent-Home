package tk.hiddenname.smarthome.service.task.impl

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.model.task.Task
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject
import tk.hiddenname.smarthome.service.task.impl.listener.EventListener
import tk.hiddenname.smarthome.service.task.impl.listener.ListenerFactory
import tk.hiddenname.smarthome.service.task.impl.processor.EventProcessor
import tk.hiddenname.smarthome.service.task.impl.processor.ProcessorFactory

@Component
@Scope("prototype")
class TaskManager(
    private val context: ApplicationContext,
    private val listenerFactory: ListenerFactory,
    private val processorFactory: ProcessorFactory
) {

    private final lateinit var listener: EventListener
    final lateinit var processor: EventProcessor

    fun register(task: Task): TaskManager {
        listener = context.getBean(EventListener::class.java, this, listenerFactory)
        processor = context.getBean(EventProcessor::class.java, this, processorFactory)

        registerListeners(task.triggerObjects)
        registerProcessors(task.processingObjects)
        return this
    }

    fun unregister() {
        unregisterListeners()
        unregisterProcessors()
    }

    private fun registerListeners(triggerObjects: List<TriggerObject>) {
        listener.registerListeners(triggerObjects)
    }

    @Suppress("unused")
    fun registerListener(triggerObject: TriggerObject) {
        listener.registerListener(triggerObject)
    }

    private fun unregisterListeners() {
        listener.unregisterListeners()
    }

    fun unregisterListener(id: Long) {
        listener.unregisterListener(id)
    }

    private fun registerProcessors(processingObjects: List<ProcessingObject>) {
        processor.registerProcessors(processingObjects)
    }

    @Suppress("unused")
    fun registerProcessor(processingObject: ProcessingObject) {
        processor.registerProcessor(processingObject)
    }

    private fun unregisterProcessors() {
        processor.unregisterProcessors()
    }

    fun unregisterProcessor(id: Long) {
        processor.unregisterProcessor(id)
    }

    fun getListenersCount() = listener.listenersCount()

    fun getProcessorsCount() = processor.getProcessorsCount()
}