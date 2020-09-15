package tk.hiddenname.smarthome.entity.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;

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
    private Integer gpio;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SignalType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GPIOMode mode;
}