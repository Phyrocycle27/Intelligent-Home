package tk.hiddenname.smarthome.service.task.impl.listener.impl;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.task.trigger.objects.ChangeDigitalSignalObject;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.repository.SensorRepository;
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorService;
import tk.hiddenname.smarthome.service.task.impl.listener.EventListener;
import tk.hiddenname.smarthome.service.task.impl.listener.Listener;

@Component
@AllArgsConstructor
@Scope(scopeName = "prototype")
public class ChangeDigitalSignalListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(ChangeDigitalSignalListener.class);

    private final DigitalSensorService service;
    private final SensorRepository repository;
    private final EventListener listener;

    private ChangeDigitalSignalObject object;
    private GpioPinListenerDigital gpioListener;

    @Override
    public void register(TriggerObject object) throws UnsupportedTriggerObjectTypeException {
        if (object instanceof ChangeDigitalSignalObject) {
            this.object = (ChangeDigitalSignalObject) object;
            Sensor sensor = repository.findById(object.getId())
                    .orElseThrow(() -> new SensorNotFoundException(object.getId()));
            gpioListener = service.addListener(this, sensor.getId(), this.object.isTargetState(), sensor.getReverse());
        } else {
            throw new UnsupportedTriggerObjectTypeException(object.getClass().getSimpleName());
        }
    }

    @Override
    public void update(TriggerObject object) throws UnsupportedTriggerObjectTypeException {
        unregister();
        register(object);
    }

    @Override
    public void unregister() {
        service.removeListener(gpioListener, object.getSensorId());
    }

    @Override
    public void trigger(boolean flag) {
        try {
            listener.updateFlag(object.getId(), flag);
        } catch (TriggerNotFoundException e) {
            log.error(e.getMessage());
            unregister();
        }
    }
}
