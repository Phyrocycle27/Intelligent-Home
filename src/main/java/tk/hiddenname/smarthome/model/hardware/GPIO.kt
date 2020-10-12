package tk.hiddenname.smarthome.model.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.model.signal.SignalType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GPIO {

    @NotNull
    private int gpioPin;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SignalType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GPIOMode mode;
}