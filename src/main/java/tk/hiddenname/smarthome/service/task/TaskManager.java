package tk.hiddenname.smarthome.service.task;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.Task;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.NoSuchListenerException;
import tk.hiddenname.smarthome.exception.NoSuchProcessorException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.service.task.listener.EventListener;
import tk.hiddenname.smarthome.service.task.listener.ListenerFactory;
import tk.hiddenname.smarthome.service.task.processor.EventProcessor;
import tk.hiddenname.smarthome.service.task.processor.ProcessorFactory;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class TaskManager {

    private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

    private final Map<Integer, EventProcessor> processors = new HashMap<>();
    private final Map<Integer, EventListener> listeners = new HashMap<>();
    private final ProcessorFactory processorFactory;
    private final ListenerFactory listenerFactory;

    public EventProcessor getProcessor(Integer id) {
        return processors.getOrDefault(id, null);
    }

    public EventListener getListener(Integer id) {
        return listeners.getOrDefault(id, null);
    }

    public void add(Task task) {
        // Processors are registering
        EventProcessor processor = new EventProcessor();

        for (ProcessingObject obj : task.getProcessingObjects()) {
            try {
                processor.add(obj.getId(), processorFactory.create(obj));
            } catch (NoSuchProcessorException | UnsupportedObjectTypeException e) {
                log.error(e.getMessage());
            }
        }

        // Listeners are registering
        EventListener listener = new EventListener(task.getId(), processor);

        for (TriggerObject obj : task.getTriggerObjects()) {
            try {
                listener.add(obj.getId(), listenerFactory.create(obj, listener));
            } catch (NoSuchListenerException | UnsupportedObjectTypeException e) {
                log.error(e.getMessage());
            }
        }

        processors.put(task.getId(), processor);
        listeners.put(task.getId(), listener);
    }
}
