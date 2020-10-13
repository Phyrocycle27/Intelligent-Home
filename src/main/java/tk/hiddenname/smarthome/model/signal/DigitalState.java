package tk.hiddenname.smarthome.model.signal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(of = {"digitalState"}, callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DigitalState extends Signal {

    public boolean isDigitalState() {
        return digitalState;
    }

    public boolean digitalState;

    public DigitalState(Long id, boolean digitalState) {
        super(id);
        this.digitalState = digitalState;
    }
}