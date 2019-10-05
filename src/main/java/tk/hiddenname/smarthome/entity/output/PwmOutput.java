package tk.hiddenname.smarthome.entity.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pwm_outputs")
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "name", "gpioNumber", "reverse", "creationDate"})
public class PwmOutput {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column
    String name;

    @Column(name = "gpio_number")
    Integer gpioNumber;

    @Column
    Boolean reverse;

    @Column(updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate;
}