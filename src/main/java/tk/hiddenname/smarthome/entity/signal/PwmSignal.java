package tk.hiddenname.smarthome.entity.signal;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ToString(of = {"pwmSignal"}, callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PwmSignal extends Signal {

    @NotNull
    @Getter
    private final int pwmSignal;

    public PwmSignal(Integer id, Integer pwmSignal) {
        super(id);
        this.pwmSignal = pwmSignal;
    }
}
