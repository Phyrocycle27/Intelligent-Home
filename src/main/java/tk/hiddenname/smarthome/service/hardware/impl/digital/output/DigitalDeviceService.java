package tk.hiddenname.smarthome.service.hardware.impl.digital.output;

import tk.hiddenname.smarthome.model.signal.DigitalState;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;

public interface DigitalDeviceService extends GPIOService {

    DigitalState getState(Long id, Boolean reverse);

    DigitalState setState(Long id, Boolean reverse, Boolean newState);
}
