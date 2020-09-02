package tk.hiddenname.smarthome.service.task.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.NoSuchListenerException;
import tk.hiddenname.smarthome.exception.NoSuchProcessorException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.service.task.impl.listener.EventListener;
import tk.hiddenname.smarthome.service.task.impl.listener.ListenerFactory;
import tk.hiddenname.smarthome.service.task.impl.processor.EventProcessor;
import tk.hiddenname.smarthome.service.task.impl.processor.ProcessorFactory;

import java.util.Set;

@Component
@Scope("prototype")
@AllArgsConstructor
public class TaskObject {

    private static final Logger log = LoggerFactory.getLogger(TaskObject.class);

    private final EventListener listener = new EventListener(this);
    private final EventProcessor processor = new EventProcessor();

    private final ProcessorFactory processorFactory;
    private final ListenerFactory listenerFactory;

    public EventProcessor getProcessor() {
        return processor;
    }

    public TaskObject register(Task task) {
        registerListeners(task.getTriggerObjects());
        registerProcessors(task.getProcessingObjects());
        return this;
    }

    private void registerListeners(Set<TriggerObject> triggerObjects) {
        for (TriggerObject obj : triggerObjects) {
            try {
                listener.add(obj.getId(), listenerFactory.create(obj, listener));
            } catch (NoSuchListenerException | UnsupportedObjectTypeException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void registerProcessors(Set<ProcessingObject> processingObjects) {
        for (ProcessingObject obj : processingObjects) {
            try {
                processor.add(obj.getId(), processorFactory.create(obj));
            } catch (NoSuchProcessorException | UnsupportedObjectTypeException e) {
                log.error(e.getMessage());
            }
        }
    }
}
