package tk.hiddenname.smarthome.model.signal;

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
    public final int pwmSignal;

    public PwmSignal(Long id, Integer pwmSignal) {
        super(id);
        this.pwmSignal = pwmSignal;
    }
}
