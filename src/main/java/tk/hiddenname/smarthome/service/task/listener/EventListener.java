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

    private final Map<Integer, Boolean> flags = new HashMap<>();
    private final Map<Integer, Listener> listeners = new HashMap<>();

    private final Integer taskId;
    private final EventProcessor processor;

    public void add(Integer id, Listener listener) throws TriggerExistsException {
        if (!listeners.containsKey(id)) {
            listeners.put(id, listener);
            flags.put(id, false);
        } else {
            throw new TriggerExistsException(id);
        }
    }

    public void remove(Integer id) throws TriggerNotFoundException {
        if (listeners.containsKey(id)) {
            listeners.get(id).unregister();
            listeners.remove(id);
            flags.remove(id);
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    public void updateFlag(Integer id, Boolean flag) {
        if (listeners.containsKey(id)) {
            flags.put(id, flag);
            if (check()) {
                processor.process();
            }
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    private boolean check() {
        return !flags.containsValue(false);
    }
}
