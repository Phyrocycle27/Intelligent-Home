package tk.hiddenname.smarthome.service.task.impl.processor

import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject

interface Processor {

    fun process()

    fun register(processingObject: ProcessingObject)
}