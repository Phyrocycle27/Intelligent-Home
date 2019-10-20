package tk.hiddenname.smarthome.service.pwm;

import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.PwmSignal;

public interface PwmOutputService {

    PwmSignal getSignal(Output output);

    PwmSignal setSignal(Output output, Integer newSignal);
}
