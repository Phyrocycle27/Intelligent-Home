package tk.hiddenname.smarthome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "name", "gpioNumber", "reverse", "creationDate"})
class Output {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    @Column(name = "gpio_number")
    Integer gpioNumber;

    @Setter
    @Getter
    Boolean reverse;

    @Getter
    @Setter
    @Column(updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate;
}
