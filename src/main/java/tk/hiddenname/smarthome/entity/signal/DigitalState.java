package tk.hiddenname.smarthome.entity.signal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ToString(of = {"digitalState"}, callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DigitalState extends Signal {

    @NotNull
    @Getter
    private final boolean digitalState;

    public DigitalState(Integer id, boolean digitalState) {
        super(id);
        this.digitalState = digitalState;
    }
}