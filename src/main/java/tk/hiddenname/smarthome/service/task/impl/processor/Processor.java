package tk.hiddenname.smarthome.service.task.impl.processor;

import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;

public interface Processor {

    void process();

    void register(ProcessingObject object) throws UnsupportedObjectTypeException;
}
