package tk.hiddenname.smarthome.entity.output;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ANALOG_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"signal"})
public class AnalogOutput extends Output {

    @Getter
    @Setter
    private Double signal;
}
