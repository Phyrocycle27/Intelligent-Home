package tk.hiddenname.smarthome.service.hardware.impl;

import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

public interface GPIOService {

    void delete(Long id);

    void save(Long id, int gpioPin, boolean reverse) throws GPIOBusyException,
            PinSignalSupportException;

    void update(Long id, boolean reverse);
}
