package tk.hiddenname.smarthome.service.task.impl.listener.impl;

import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.exception.TriggerNotFoundException;
import tk.hiddenname.smarthome.exception.UnsupportedTriggerObjectTypeException;
import tk.hiddenname.smarthome.model.hardware.Sensor;
import tk.hiddenname.smarthome.model.task.trigger.objects.ChangeDigitalSignalObject;
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject;
import tk.hiddenname.smarthome.service.database.SensorDatabaseService;
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
    private SensorDatabaseService dbService;

    private ChangeDigitalSignalObject object;
    private GpioPinListenerDigital gpioListener;
    private DelayCounterThread delayCounter;

    @Autowired
    public void setService(DigitalSensorService service) {
        this.service = service;
    }

    @Autowired
    public void setDbService(SensorDatabaseService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void register(@NotNull TriggerObject object) throws UnsupportedTriggerObjectTypeException {
        if (object instanceof ChangeDigitalSignalObject) {
            this.object = (ChangeDigitalSignalObject) object;
            Sensor sensor = dbService.getOne(this.object.getSensorId());
            gpioListener = service.addListener(this, sensor.getId(), this.object.isTargetState(),
                    sensor.isReverse());
        } else {
            throw new UnsupportedTriggerObjectTypeException(object.getClass().getSimpleName());
        }
    }

    @Override
    public void update(@NotNull TriggerObject object) throws UnsupportedTriggerObjectTypeException {
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

    @SuppressWarnings("BusyWait")
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
                    //noinspection BusyWait
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
