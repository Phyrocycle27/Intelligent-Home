package tk.hiddenname.smarthome.service.task.listener;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.exception.TriggerExistsException;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;
import tk.hiddenname.smarthome.service.task.processor.EventProcessor;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(of = {"taskId"})
@AllArgsConstructor
public class EventListener {

    private final Map<Integer, Boolean> triggers = new HashMap<>();

    private final Integer taskId;
    private final EventProcessor processor;

    public void add(Integer id) throws TriggerExistsException {
        if (!triggers.containsKey(id)) {
            triggers.put(id, false);
        } else {
            throw new TriggerExistsException(id);
        }
    }

    public void delete(Integer id) throws TriggerNotFoundException {
        if (triggers.containsKey(id)) {
            triggers.remove(id);
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    public void update(Integer id, boolean status) throws TriggerNotFoundException {
        if (triggers.containsKey(id)) {
            triggers.replace(id, status);
            if (check()) {
                processor.process();
            }
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    private boolean check() {
        return !triggers.containsValue(false);
    }
}
