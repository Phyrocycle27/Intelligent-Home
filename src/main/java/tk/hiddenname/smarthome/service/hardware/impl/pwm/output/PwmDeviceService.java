package tk.hiddenname.smarthome.service.hardware.impl.pwm.output;

import tk.hiddenname.smarthome.model.signal.PwmSignal;
import tk.hiddenname.smarthome.service.hardware.impl.GPIOService;

public interface PwmDeviceService extends GPIOService {

    PwmSignal getSignal(Long id, boolean reverse);

    PwmSignal setSignal(Long id, boolean reverse, int newSignal);
}
