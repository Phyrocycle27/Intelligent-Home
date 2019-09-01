package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.entities.Signal;
import tk.hiddenname.smarthome.exception.OutputNotFoundException;

public interface ControlOutputService {
    Signal getSignal(Integer id) throws OutputNotFoundException;

    void updateSignal(Integer id, Signal signal) throws OutputNotFoundException;
}
