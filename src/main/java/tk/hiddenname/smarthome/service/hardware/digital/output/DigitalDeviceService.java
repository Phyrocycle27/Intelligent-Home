package tk.hiddenname.smarthome.service.hardware.digital.output;

import tk.hiddenname.smarthome.entity.signal.DigitalState;
import tk.hiddenname.smarthome.service.hardware.GPIOService;

public interface DigitalDeviceService extends GPIOService {

    DigitalState getState(Integer id, Boolean reverse);

    DigitalState setState(Integer id, Boolean reverse, Boolean newState);
}
