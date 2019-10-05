package tk.hiddenname.smarthome.entity.output;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PWM_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"signal"})
public class PwmOutput extends Output {

    private Integer signal;
}