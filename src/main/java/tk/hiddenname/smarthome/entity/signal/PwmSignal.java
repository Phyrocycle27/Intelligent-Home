package tk.hiddenname.smarthome.entity.signal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(of = {"pwmSignal"}, callSuper = true)
public class PwmSignal extends Signal {

    @Getter
    private Integer pwmSignal;

    public PwmSignal(Integer id, Integer pwmSignal) {
        super(id);
        this.pwmSignal = pwmSignal;
    }
}
