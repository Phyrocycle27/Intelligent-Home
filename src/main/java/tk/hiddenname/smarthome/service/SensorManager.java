package tk.hiddenname.smarthome.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.Device;
import tk.hiddenname.smarthome.entity.GPIO;
import tk.hiddenname.smarthome.entity.GPIOType;
import tk.hiddenname.smarthome.entity.Sensor;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.service.digital.input.DigitalSensorServiceImpl;

@Component
@AllArgsConstructor
public class SensorManager {

    private static final Logger log = LoggerFactory.getLogger(SensorManager.class);

    private final DigitalSensorServiceImpl digitalService;

    public void create(Sensor sensor) throws PinSignalSupportException, GPIOBusyException {
        log.debug("Creating device " + sensor.toString());
        GPIO gpio = sensor.getGpio();
        getService(gpio.getType()).save(
                gpio.getId(),
                gpio.getGpio(),
                sensor.getReverse()
        );
    }

    public void update(Device device) {
        log.debug("Updating device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).update(
                gpio.getId(),
                device.getReverse()
        );
    }

    public void delete(Sensor device) {
        log.debug("Deleting device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).delete(
                gpio.getId()
        );
    }

    private GPIOService getService(GPIOType type) {
        if (type == GPIOType.DIGITAL) {
            return digitalService;
        }
        TypeNotFoundException e = new TypeNotFoundException(type.toString());
        log.warn(e.getMessage());
        throw e;
    }
}
