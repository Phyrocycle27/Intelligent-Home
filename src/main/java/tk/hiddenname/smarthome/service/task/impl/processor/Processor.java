package tk.hiddenname.smarthome.service.task.impl.processor;

import tk.hiddenname.smarthome.exception.UnsupportedProcessingObjectTypeException;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject;

public interface Processor {

    void process();

    void register(ProcessingObject object) throws UnsupportedTriggerObjectTypeException, UnsupportedProcessingObjectTypeException;
}
