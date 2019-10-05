package tk.hiddenname.smarthome.entity.output;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DIGITAL_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"state"})
public class DigitalOutput extends Output {

    private Boolean state;
}
