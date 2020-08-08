package tk.hiddenname.smarthome.service.task.listener;

import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;

public interface Listener {

    void update(boolean flag);
    void register(TriggerObject object) throws UnsupportedObjectTypeException;
    void unregister();
}
