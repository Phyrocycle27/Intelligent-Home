package tk.hiddenname.smarthome.service.digital;

import tk.hiddenname.smarthome.entity.signal.DigitalState;

public interface DigitalDeviceService {

    DigitalState getState(Integer id, Boolean reverse);

    DigitalState setState(Integer id, Boolean reverse, Boolean newState);
}
