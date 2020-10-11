package tk.hiddenname.smarthome.service.task.impl.processor

import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject
import javax.validation.constraints.NotNull

interface Processor {

    fun process()

    @Throws(UnsupportedProcessingObjectTypeException::class)
    fun register(processingObject: @NotNull ProcessingObject)
}