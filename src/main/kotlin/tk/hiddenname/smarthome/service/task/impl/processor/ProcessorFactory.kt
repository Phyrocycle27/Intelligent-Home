package tk.hiddenname.smarthome.service.task.impl.processor

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import tk.hiddenname.smarthome.exception.support.NoSuchProcessorException
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetDigitalSignalProcessor
import tk.hiddenname.smarthome.service.task.impl.processor.impl.SetPwmSignalProcessor

@Component
class ProcessorFactory(private val ctx: ApplicationContext) {

    fun create(processingObject: ProcessingObject): Processor {
        val processor = when (processingObject.action) {
            ProcessingAction.SET_DIGITAL_SIGNAL -> ctx.getBean(SetDigitalSignalProcessor::class.java)
            ProcessingAction.SET_PWM_SIGNAL -> ctx.getBean(SetPwmSignalProcessor::class.java)
            else -> throw NoSuchProcessorException()
        }
        processor.register(processingObject)
        return processor
    }
}