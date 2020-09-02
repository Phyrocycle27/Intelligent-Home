package tk.hiddenname.smarthome.service.hardware.manager;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.hardware.GPIO;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;
import tk.hiddenname.smarthome.exception.SignalTypeNotFoundException;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;
import tk.hiddenname.smarthome.service.hardware.impl.digital.output.DigitalDeviceServiceImpl;
import tk.hiddenname.smarthome.service.hardware.impl.pwm.output.PwmDeviceServiceImpl;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class DeviceManager {

    private final Logger log = LoggerFactory.getLogger(DeviceManager.class);

    @NotNull
    private final DigitalDeviceServiceImpl digitalService;

    @NotNull
    private final PwmDeviceServiceImpl pwmService;

    public void create(Device device) throws PinSignalSupportException, GPIOBusyException {
        log.debug("Creating device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).save(
                device.getId(),
                gpio.getGpio(),
                device.isReverse()
        );
    }

    public void update(Device device) {
        log.debug("Updating device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).update(
                device.getId(),
                device.isReverse()
        );
    }

    public void delete(Device device) {
        log.debug("Deleting device " + device.toString());
        GPIO gpio = device.getGpio();
        getService(gpio.getType()).delete(
                device.getId()
        );
    }

    private GPIOService getService(SignalType type) {
        switch (type) {
            case DIGITAL:
                return digitalService;
            case PWM:
                return pwmService;
            default:
                SignalTypeNotFoundException e = new SignalTypeNotFoundException(type.toString());
                log.warn(e.getMessage());
                throw e;
        }
    }
}
