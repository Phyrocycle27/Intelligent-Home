package tk.hiddenname.smarthome.service.task.listener;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.task.trigger.objects.ChangeSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.NoSuchListenerException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedObjectTypeException;
import tk.hiddenname.smarthome.service.task.listener.impl.ChangeDigitalSignalListener;

@Component
@AllArgsConstructor
public class ListenerFactory {

    private final ApplicationContext ctx;

    public Listener create(@NonNull TriggerObject object) throws NoSuchListenerException, UnsupportedObjectTypeException {
        Listener listener;

        switch (object.getAction()) {
            case CHANGE_SIGNAL:
                switch (((ChangeSignalObject) object).getSignalType()) {
                    case PWM:
                        throw new NoSuchListenerException();
                    case DIGITAL:
                        listener = ctx.getBean(ChangeDigitalSignalListener.class);
                        break;
                    default:
                        throw new SignalTypeNotFoundException(((ChangeSignalObject) object).getSignalType().name());
                }
                break;
            default:
                throw new NoSuchListenerException();
        }

        listener.register(object);
        return listener;
    }
}