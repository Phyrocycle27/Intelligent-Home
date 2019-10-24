package tk.hiddenname.smarthome.entity.signal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"pwmSignal"}, callSuper = true)
public class PwmSignal extends Signal {

    private Integer pwmSignal;

    public PwmSignal(Integer outputId, Integer signal) {
        super(outputId);
        this.pwmSignal = signal;
    }

    public Integer getPwmSignal() {
        return pwmSignal;
    }

    public void setPwmSignal(Integer pwmSignal) {
        this.pwmSignal = pwmSignal;
    }
}
