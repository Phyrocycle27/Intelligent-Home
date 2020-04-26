package tk.hiddenname.smarthome.entity.signal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignalObject {

    private SignalType type;
    private int value;
}
