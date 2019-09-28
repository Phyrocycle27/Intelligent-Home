package tk.hiddenname.smarthome.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "DIGITAL_OUTPUTS")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, of = {"state"})
public class DigitalOutput extends Output {

    @Getter
    @Setter
    private Boolean state;
}
