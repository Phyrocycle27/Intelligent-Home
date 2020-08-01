package tk.hiddenname.smarthome.service.hardware.pwm;

import tk.hiddenname.smarthome.entity.signal.PwmSignal;

public interface PwmDeviceService {

    PwmSignal getSignal(Integer id, Boolean reverse);

    PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal);
}
