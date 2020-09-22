package tk.hiddenname.smarthome.service.hardware.manager;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.GPIO;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.service.database.SensorDatabaseService;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;
import tk.hiddenname.smarthome.service.hardware.impl.digital.input.DigitalSensorServiceImpl;

@Component
@AllArgsConstructor
public class SensorManager {

    private static final Logger log = LoggerFactory.getLogger(SensorManager.class);

    private final DigitalSensorServiceImpl digitalService;
    private final SensorDatabaseService service;

    public void create(Sensor sensor) throws PinSignalSupportException, GPIOBusyException {
        log.debug("Creating device " + sensor.toString());
        GPIO gpio = sensor.getGpio();
        getService(gpio.getType()).save(
                sensor.getId(),
                gpio.getGpio(),
                sensor.isReverse()
        );
    }

    public void delete(Sensor sensor) {
        log.debug("Deleting sensor " + sensor.toString());
        GPIO gpio = sensor.getGpio();
        getService(gpio.getType()).delete(
                sensor.getId()
        );
    }

    public void loadSensors() {
        for (Sensor sensor : service.getAll()) {
            try {
                create(sensor);
            } catch (PinSignalSupportException | GPIOBusyException e) {
                log.warn(e.getMessage());
            }
        }
    }

    private GPIOService getService(SignalType type) {
        if (type == SignalType.DIGITAL) {
            return digitalService;
        }
        SignalTypeNotFoundException e = new SignalTypeNotFoundException(type.toString());
        log.warn(e.getMessage());
        throw e;
    }
}
