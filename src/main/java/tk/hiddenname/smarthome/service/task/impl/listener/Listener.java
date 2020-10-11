package tk.hiddenname.smarthome.service.task.impl.listener;

import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject;

public interface Listener {

    void register(TriggerObject object) throws UnsupportedTriggerObjectTypeException;

    void update(TriggerObject object) throws UnsupportedTriggerObjectTypeException;

    void unregister();

    void trigger(boolean flag);
}
