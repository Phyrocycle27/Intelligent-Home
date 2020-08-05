package tk.hiddenname.smarthome.entity.signal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(of = {"digitalState"}, callSuper = true)
public class DigitalState extends Signal {

    @Getter
    private final boolean digitalState;

    public DigitalState(Integer id, boolean digitalState) {
        super(id);
        this.digitalState = digitalState;
    }
}