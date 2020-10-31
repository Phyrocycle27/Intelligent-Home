package tk.hiddenname.smarthome.service.task.impl

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.*
import tk.hiddenname.smarthome.exception.not_found.ProcessorNotFoundException
import tk.hiddenname.smarthome.exception.not_found.TriggerNotFoundException
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

    @Throws(TriggerExistsException::class, UnsupportedTriggerObjectTypeException::class,
            NoSuchListenerException::class, NoSuchProcessorException::class, ProcessorExistsException::class,
            UnsupportedProcessingObjectTypeException::class)
    fun register(task: Task): TaskManager {
        registerListeners(task.triggerObjects)
        registerProcessors(task.processingObjects)
        return this
    }

    fun unregister() {
        unregisterListeners()
        unregisterProcessors()
    }

    @Throws(TriggerExistsException::class, UnsupportedTriggerObjectTypeException::class,
            NoSuchListenerException::class)
    fun registerListeners(triggerObjects: Set<TriggerObject>) {
        listener.registerListeners(triggerObjects)
    }

    @Throws(TriggerExistsException::class, UnsupportedTriggerObjectTypeException::class,
            NoSuchListenerException::class)
    fun registerListener(triggerObject: TriggerObject) {
        listener.registerListener(triggerObject)
    }

    fun unregisterListeners() {
        listener.unregisterListeners()
    }

    @Throws(TriggerNotFoundException::class)
    fun unregisterListener(id: Long) {
        listener.unregisterListener(id)
    }

    @Throws(NoSuchProcessorException::class, ProcessorExistsException::class,
            UnsupportedProcessingObjectTypeException::class)
    fun registerProcessors(processingObjects: Set<ProcessingObject>) {
        processor.registerProcessors(processingObjects)
    }

    @Throws(NoSuchProcessorException::class, ProcessorExistsException::class,
            UnsupportedProcessingObjectTypeException::class)
    fun registerProcessor(processingObject: ProcessingObject) {
        processor.registerProcessor(processingObject)
    }

    fun unregisterProcessors() {
        processor.unregisterProcessors()
    }

    @Throws(ProcessorNotFoundException::class)
    fun unregisterProcessor(id: Long) {
        processor.unregisterProcessor(id)
    }
}