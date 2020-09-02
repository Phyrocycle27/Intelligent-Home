package tk.hiddenname.smarthome.service.task.impl.listener;

import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;

public interface Listener {

    void register(TriggerObject object) throws UnsupportedObjectTypeException;

    void update(TriggerObject object) throws UnsupportedObjectTypeException;

    void unregister();

    void trigger(boolean flag);
}
