package tk.hiddenname.smarthome.service.task.impl.processor

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.exist.ProcessorExistsException
import tk.hiddenname.smarthome.exception.not_found.ProcessorNotFoundException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject

@Component
@Scope("prototype")
class EventProcessor(private val processorFactory: ProcessorFactory) {

    private val log = LoggerFactory.getLogger(EventProcessor::class.java)

    private val processors = HashMap<Long, Processor>()

    fun registerProcessors(processingObjects: List<ProcessingObject>) {
        processingObjects.forEach {
            registerProcessor(it)
        }
    }

    fun registerProcessor(processingObject: ProcessingObject) {
        if (processors.containsKey(processingObject.id)) {
            throw ProcessorExistsException(processingObject.id)
        } else {
            processors[processingObject.id] = processorFactory.create(processingObject)
        }
    }

    fun unregisterProcessors() {
        processors.keys.forEach {
            try {
                unregisterProcessor(it)
            } catch (e: ProcessorNotFoundException) {
                log.warn(e.message)
            }
        }
    }

    fun unregisterProcessor(id: Long) {
        if (processors.containsKey(id)) {
            processors.remove(id)
        } else {
            throw ProcessorNotFoundException(id)
        }
    }

    fun process() {
        processors.values.forEach {
            it.process()
        }
    }
}