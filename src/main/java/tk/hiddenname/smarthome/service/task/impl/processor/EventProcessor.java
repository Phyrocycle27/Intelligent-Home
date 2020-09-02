package tk.hiddenname.smarthome.service.task.impl.processor;

import lombok.RequiredArgsConstructor;
import tk.hiddenname.smarthome.exception.ProcessorExistsException;
import tk.hiddenname.smarthome.exception.ProcessorNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class EventProcessor {

    private final Map<Integer, Processor> processors = new HashMap<>();

    public void add(Integer id, Processor processor) throws ProcessorExistsException {
        if (!processors.containsKey(id)) {
            processors.put(id, processor);
        } else {
            throw new ProcessorExistsException(id);
        }
    }

    public void delete(Integer id) throws ProcessorNotFoundException {
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
