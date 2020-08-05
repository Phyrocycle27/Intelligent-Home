package tk.hiddenname.smarthome.service.manager;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.GPIO;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.service.hardware.GPIOService;
import tk.hiddenname.smarthome.service.hardware.digital.input.DigitalSensorServiceImpl;

@Component
@RequiredArgsConstructor
public class SensorManager {

    private final Logger log = LoggerFactory.getLogger(SensorManager.class);

    @NonNull
    private final DigitalSensorServiceImpl digitalService;

    public void create(Sensor sensor) throws PinSignalSupportException, GPIOBusyException {
        log.debug("Creating device " + sensor.toString());
        GPIO gpio = sensor.getGpio();
        getService(gpio.getType()).save(
                sensor.getId(),
                gpio.getGpio(),
                sensor.getReverse()
        );
    }

    public void delete(Sensor sensor) {
        log.debug("Deleting sensor " + sensor.toString());
        GPIO gpio = sensor.getGpio();
        getService(gpio.getType()).delete(
                sensor.getId()
        );
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
