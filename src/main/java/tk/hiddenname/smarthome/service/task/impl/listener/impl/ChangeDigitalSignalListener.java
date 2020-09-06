package tk.hiddenname.smarthome.service.task.impl.listener.impl;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor
@Scope(scopeName = "prototype")
public class ChangeDigitalSignalListener implements Listener {

    private static final Logger log = LoggerFactory.getLogger(ChangeDigitalSignalListener.class);

    @NonNull
    private final EventListener listener;

    private DigitalSensorService service;
    private SensorRepository repository;

    private ChangeDigitalSignalObject object;
    private GpioPinListenerDigital gpioListener;
    private DelayCounterThread delayCounter;

    @Autowired
    public void setService(DigitalSensorService service) {
        this.service = service;
    }

    @Autowired
    public void setRepository(SensorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void register(TriggerObject object) throws UnsupportedTriggerObjectTypeException {
        if (object instanceof ChangeDigitalSignalObject) {
            this.object = (ChangeDigitalSignalObject) object;
            Sensor sensor = repository.findById(this.object.getSensorId())
                    .orElseThrow(() -> new SensorNotFoundException(object.getId()));
            gpioListener = service.addListener(this, sensor.getId(), this.object.isTargetState(),
                    sensor.getReverse());
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
        if (delayCounter == null) {
            if (flag) {
                delayCounter = new DelayCounterThread(object.getDelay());
                delayCounter.setDaemon(true);
                delayCounter.start();
            }
        } else {
            if (!flag) {
                delayCounter.disable();
                setEventListenerFlag(false);
                delayCounter = null;
            }
        }
    }

    private void setEventListenerFlag(boolean flag) {
        try {
            listener.updateFlag(object.getId(), flag);
        } catch (TriggerNotFoundException e) {
            unregister();
        }
    }

    private class DelayCounterThread extends Thread {

        private int delay;
        private boolean status;

        public DelayCounterThread(int delay) {
            this.delay = delay;
            this.status = true;
        }

        private void disable() {
            status = false;
        }

        @Override
        public void run() {
            while (status && delay > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                delay--;
            }
            if (delay == 0) {
                setEventListenerFlag(true);
            }
        }
    }
}
