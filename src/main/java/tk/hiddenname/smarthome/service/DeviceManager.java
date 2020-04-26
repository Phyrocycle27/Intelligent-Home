package tk.hiddenname.smarthome.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.GPIO;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.service.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmDeviceServiceImpl;

@Component
@AllArgsConstructor
public class DeviceManager {

    private static final Logger log = LoggerFactory.getLogger(DeviceManager.class);
    private static DigitalDeviceServiceImpl digitalService;
    private static PwmDeviceServiceImpl pwmService;

    public void create(Device device) throws PinSignalSupportException, GPIOBusyException {
        log.debug("Creating device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).save(
                gpio.getId(),
                gpio.getGpio(),
                device.getReverse()
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

    public void delete(Device device) {
        log.debug("Deleting device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).delete(
                gpio.getId()
        );
    }

    private GPIOService getService(SignalType type) {
        switch (type) {
            case DIGITAL:
                return digitalService;
            case PWM:
                return pwmService;
            default:
                TypeNotFoundException e = new TypeNotFoundException(type.toString());
                log.warn(e.getMessage());
                throw e;
        }
    }
}
