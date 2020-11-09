package tk.hiddenname.smarthome.service.task.impl

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.not_found.ProcessorNotFoundException
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
        context: ApplicationContext,
        listenerFactory: ListenerFactory,
        processorFactory: ProcessorFactory) {

    private val listener = context.getBean(EventListener::class.java, this, listenerFactory)
    val processor: EventProcessor = context.getBean(EventProcessor::class.java, processorFactory)

    fun register(task: Task): TaskManager {
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

    @Suppress("unused")
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

    @Suppress("unused")
    @Throws(ProcessorNotFoundException::class)
    fun unregisterProcessor(id: Long) {
        processor.unregisterProcessor(id)
    }
}