package tk.hiddenname.smarthome.service.task.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.*;
import tk.hiddenname.smarthome.service.task.impl.listener.EventListener;
import tk.hiddenname.smarthome.service.task.impl.processor.EventProcessor;
import tk.hiddenname.smarthome.service.task.impl.processor.ProcessorFactory;

import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class TaskObject {

    private static final Logger log = LoggerFactory.getLogger(TaskObject.class);

    @NonNull
    private final ProcessorFactory processorFactory;
    @NonNull
    private final ApplicationContext context;


    private EventListener listener;
    private EventProcessor processor;

    public EventProcessor getProcessor() {
        return processor;
    }

    public TaskObject register(Task task) throws TriggerExistsException, UnsupportedTriggerObjectTypeException,
            NoSuchListenerException, NoSuchProcessorException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {

        listener = context.getBean(EventListener.class, this);
        processor = context.getBean(EventProcessor.class);

        registerListeners(task.getTriggerObjects());
        registerProcessors(task.getProcessingObjects());

        return this;
    }

    public void unregister() {
        unregisterListeners();
        unregisterProcessors();
    }

    public void registerListeners(Set<TriggerObject> triggerObjects) throws TriggerExistsException,
            UnsupportedTriggerObjectTypeException, NoSuchListenerException {

        listener.registerListeners(triggerObjects);
    }

    public void registerListener(TriggerObject triggerObject) throws TriggerExistsException,
            UnsupportedTriggerObjectTypeException, NoSuchListenerException {

        listener.registerListener(triggerObject);
    }

    public void unregisterListeners() {
        listener.unregisterListeners();
    }

    public void unregisterListener(Integer id) throws TriggerNotFoundException {
        listener.unregisterListener(id);
    }

    public void registerProcessors(Set<ProcessingObject> processingObjects) throws NoSuchProcessorException,
            UnsupportedTriggerObjectTypeException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {

        processor.registerProcessors(processingObjects);
    }

    public void registerProcessor(ProcessingObject object) throws NoSuchProcessorException,
            UnsupportedTriggerObjectTypeException, ProcessorExistsException, UnsupportedProcessingObjectTypeException {

        processor.registerProcessor(object);
    }

    public void unregisterProcessors() {
        processor.unregisterProcessors();
    }

    public void unregisterProcessor(Integer id) throws ProcessorNotFoundException {
        processor.unregisterProcessor(id);
    }
}
