package tk.hiddenname.smarthome.service;

import tk.hiddenname.smarthome.exception.OutputNotFoundException;

public interface ControlOutputService {
    void updateSignal(Integer id, Integer signal) throws OutputNotFoundException;
}
