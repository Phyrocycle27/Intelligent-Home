package tk.hiddenname.smarthome.entity.hardware;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GPIO {

    private Integer gpio;

    @Enumerated(EnumType.STRING)
    private SignalType type;

    @Enumerated(EnumType.STRING)
    private GPIOMode mode;
}