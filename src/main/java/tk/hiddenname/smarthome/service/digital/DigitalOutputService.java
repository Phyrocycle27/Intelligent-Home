package tk.hiddenname.smarthome.service.digital;

import tk.hiddenname.smarthome.entity.Output;
import tk.hiddenname.smarthome.entity.signal.DigitalState;

public interface DigitalOutputService {

    DigitalState getState(Output output);

    DigitalState setState(Output output, Boolean newState);
}
