package tk.hiddenname.smarthome.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ANALOG_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"signal"})
public class AnalogOutput extends Output{

    @Getter
    @Setter
    private Double signal;
}
