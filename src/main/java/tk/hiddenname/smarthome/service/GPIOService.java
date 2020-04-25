package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

public interface GPIOService {

    void delete(Integer id);

    void save(Integer id, Integer gpio, Boolean reverse) throws GPIOBusyException,
            PinSignalSupportException;

    void update(Integer id, Boolean reverse);
}
