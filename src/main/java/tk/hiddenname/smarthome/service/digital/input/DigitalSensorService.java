package tk.hiddenname.smarthome.service.digital.input;

import tk.hiddenname.smarthome.entity.signal.DigitalState;

public interface DigitalSensorService {

    DigitalState getState(Integer id, Boolean reverse);
}
