package tk.hiddenname.smarthome.service.pwm;

import tk.hiddenname.smarthome.entity.signal.PwmSignal;

public interface PwmOutputService {

    PwmSignal getSignal(Integer id);

    PwmSignal getSignal(Integer id, Boolean reverse);

    PwmSignal setSignal(Integer id, Boolean reverse, Integer newSignal);
}
