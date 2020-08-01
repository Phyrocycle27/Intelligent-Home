package tk.hiddenname.smarthome.service.task.listener;

import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;

public interface Listener {

    void update();

    void register(TriggerObject object);

    void unregister();
}
