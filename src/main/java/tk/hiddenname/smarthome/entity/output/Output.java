package tk.hiddenname.smarthome.entity.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "name", "gpioNumber", "reverse", "creationDate"})
class Output {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    String name;

    @Column(name = "gpio_number")
    Integer gpioNumber;

    Boolean reverse;

    @Column(updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate;
}
