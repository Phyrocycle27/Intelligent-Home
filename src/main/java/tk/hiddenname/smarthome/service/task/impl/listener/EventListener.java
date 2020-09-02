package tk.hiddenname.smarthome.service.task.impl.listener;

import lombok.AllArgsConstructor;
import tk.hiddenname.smarthome.exception.TriggerExistsException;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;
import tk.hiddenname.smarthome.service.task.impl.TaskObject;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class EventListener {

    private final Map<Integer, Boolean> flags = new HashMap<>();
    private final Map<Integer, Listener> listeners = new HashMap<>();

    private final TaskObject taskObject;

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
                taskObject.getProcessor().process();
            }
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    private boolean check() {
        return !flags.containsValue(false);
    }
}
