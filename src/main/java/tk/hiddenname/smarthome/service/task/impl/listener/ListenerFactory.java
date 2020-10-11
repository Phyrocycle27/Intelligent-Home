package tk.hiddenname.smarthome.service.task.impl.listener;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.exception.NoSuchListenerException;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.service.task.impl.listener.impl.ChangeDigitalSignalListener;

import javax.validation.constraints.NotNull;

@Component
@AllArgsConstructor
public class ListenerFactory {

    private final ApplicationContext ctx;

    public Listener create(@NotNull TriggerObject object, EventListener eventListener) throws NoSuchListenerException, UnsupportedTriggerObjectTypeException {
        Listener listener;

        switch (object.getAction()) {
            case CHANGE_DIGITAL_SIGNAL:
                listener = ctx.getBean(ChangeDigitalSignalListener.class, eventListener);
                break;
            default:
                throw new NoSuchListenerException();
        }

        listener.register(object);
        return listener;
    }
}
