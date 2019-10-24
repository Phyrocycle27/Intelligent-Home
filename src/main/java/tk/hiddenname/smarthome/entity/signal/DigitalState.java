package tk.hiddenname.smarthome.entity.signal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(of = {"digitalState"}, callSuper = true)
public class DigitalState extends Signal {

    private Boolean digitalState;

    public DigitalState(Integer outputId, Boolean state) {
        super(outputId);
        this.digitalState = state;
    }

    public Boolean getDigitalState() {
        return digitalState;
    }

    public void setDigitalState(Boolean digitalState) {
        this.digitalState = digitalState;
    }
}
