package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.exception.DeviceAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

public interface OutputService {

    void delete(Integer id);

    void save(Integer id, Integer gpio, Boolean reverse) throws DeviceAlreadyExistException,
            PinSignalSupportException;

    void update(Integer id, Boolean reverse);
}
