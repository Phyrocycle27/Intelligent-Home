package tk.hiddenname.smarthome.service.hardware.pwm.output;

import tk.hiddenname.smarthome.entity.signal.PwmSignal;
import tk.hiddenname.smarthome.service.hardware.GPIOService;

public interface PwmDeviceService extends GPIOService {

    PwmSignal getSignal(Integer id, Boolean reverse);

    PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal);
}
