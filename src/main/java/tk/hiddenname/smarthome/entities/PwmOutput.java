package tk.hiddenname.smarthome.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "PWM_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"signal"})
public class PwmOutput extends Output {

    @Getter
    @Setter
    private Integer signal;
}