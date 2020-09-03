package tk.hiddenname.smarthome.service.task.impl.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.exception.NoSuchProcessorException;
import tk.hiddenname.smarthome.exception.ProcessorExistsException;
import tk.hiddenname.smarthome.exception.ProcessorNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class EventProcessor {

    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);
    private final Map<Integer, Processor> processors = new HashMap<>();

    private final ProcessorFactory processorFactory;

    public void registerProcessors(Set<ProcessingObject> objects) throws NoSuchProcessorException,
            UnsupportedObjectTypeException, ProcessorExistsException {

        for (ProcessingObject object : objects) {
            registerProcessor(object);
        }
    }

    public void registerProcessor(ProcessingObject object) throws NoSuchProcessorException,
            UnsupportedObjectTypeException, ProcessorExistsException {

        if (!processors.containsKey(object.getId())) {
            processors.put(object.getId(), processorFactory.create(object));
        } else {
            throw new ProcessorExistsException(object.getId());
        }
    }

    public void unregisterProcessors() {
        for (Integer id : processors.keySet()) {
            try {
                unregisterProcessor(id);
            } catch (ProcessorNotFoundException e) {
                log.warn(e.getMessage());
            }
        }
    }

    public void unregisterProcessor(Integer id) throws ProcessorNotFoundException {
        if (processors.containsKey(id)) {
            processors.remove(id);
        } else {
            throw new ProcessorNotFoundException(id);
        }
    }

    public void process() {
        for (Processor processor : processors.values()) {
            processor.process();
        }
    }
}
