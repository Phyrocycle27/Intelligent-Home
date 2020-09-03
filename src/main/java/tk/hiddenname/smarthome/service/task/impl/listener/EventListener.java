package tk.hiddenname.smarthome.service.task.impl.listener;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.NoSuchListenerException;
import tk.hiddenname.smarthome.exception.TriggerExistsException;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.service.task.impl.TaskObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
@AllArgsConstructor
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);
    private final Map<Integer, Boolean> flags = new HashMap<>();
    private final Map<Integer, Listener> listeners = new HashMap<>();

    private final TaskObject taskObject;
    private final ListenerFactory listenerFactory;

    public void registerListeners(Set<TriggerObject> objects) throws TriggerExistsException,
            UnsupportedObjectTypeException, NoSuchListenerException {
        for (TriggerObject object : objects) {
            registerListener(object);
        }
    }

    public void registerListener(TriggerObject object) throws TriggerExistsException, UnsupportedObjectTypeException,
            NoSuchListenerException {
        if (!listeners.containsKey(object.getId())) {
            listeners.put(object.getId(), listenerFactory.create(object, this));
            flags.put(object.getId(), false);
        } else {
            throw new TriggerExistsException(object.getId());
        }
    }

    public void unregisterListeners() {
        for (Integer id : listeners.keySet()) {
            try {
                unregisterListener(id);
            } catch (TriggerNotFoundException e) {
                log.warn(e.getMessage());
            }
        }
    }

    public void unregisterListener(Integer id) throws TriggerNotFoundException {
        if (listeners.containsKey(id)) {
            listeners.get(id).unregister();
            listeners.remove(id);
            flags.remove(id);
        } else {
            throw new TriggerNotFoundException(id);
        }
    }

    public void updateFlag(Integer id, Boolean flag) throws TriggerNotFoundException {
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
