package tk.hiddenname.smarthome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.Device;
import tk.hiddenname.smarthome.entity.GPIO;
import tk.hiddenname.smarthome.entity.GPIOType;
import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.TypeNotFoundException;
import tk.hiddenname.smarthome.service.digital.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.pwm.PwmDeviceServiceImpl;

@Component
public class DeviceManager {

    public static final Logger log = LoggerFactory.getLogger(DeviceManager.class.getName());
    private static DigitalDeviceServiceImpl digitalService;
    private static PwmDeviceServiceImpl pwmService;

    @Autowired
    public DeviceManager(DigitalDeviceServiceImpl digitalService, PwmDeviceServiceImpl pwmService) {
        DeviceManager.digitalService = digitalService;
        DeviceManager.pwmService = pwmService;
        log.debug("DeviceManager's fields have just initialized");
    }

    public void create(Device device) throws PinSignalSupportException, DeviceAlreadyExistException {
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

    private GPIOService getService(GPIOType type) {
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
