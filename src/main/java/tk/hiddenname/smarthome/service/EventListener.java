package tk.hiddenname.smarthome.service;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import tk.hiddenname.smarthome.exception.TriggerExistsException;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(of = {"taskId"})
@RequiredArgsConstructor
public class EventListener {

    private final Map<Integer, Boolean> triggers = new HashMap<>();

    @NonNull
    private final Integer taskId;
    @NonNull
    private final TaskManager manager;

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
                manager.getProcessor(taskId).process();
            }
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    private boolean check() {
        return !triggers.containsValue(false);
    }
}
