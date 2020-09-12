package tk.hiddenname.smarthome.service.task.impl.listener;

import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;

public interface Listener {

    void register(TriggerObject object) throws UnsupportedTriggerObjectTypeException;

    void update(TriggerObject object) throws UnsupportedTriggerObjectTypeException;

    void unregister();

    void trigger(boolean flag);
}
