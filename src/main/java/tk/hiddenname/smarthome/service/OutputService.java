package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.exception.OutputAlreadyExistException;
import tk.hiddenname.smarthome.exception.PinSignalSupportException;

public interface OutputService {

    void delete(Integer id);

    void save(Integer id, Integer gpio, String name, Boolean reverse) throws OutputAlreadyExistException, PinSignalSupportException;

    void update(Integer id, String name);

    void update(Integer id, String name, Boolean reverse);
}
