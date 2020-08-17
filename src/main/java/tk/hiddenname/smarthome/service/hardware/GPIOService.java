package tk.hiddenname.smarthome.service.hardware;

import tk.hiddenname.smarthome.exception.GPIOBusyException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

public interface GPIOService {

    void delete(Integer id);

    void save(Integer id, Integer gpio, boolean reverse) throws GPIOBusyException,
            PinSignalSupportException;

    void update(Integer id, boolean reverse);
}
