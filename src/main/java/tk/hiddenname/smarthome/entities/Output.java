package tk.hiddenname.smarthome.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "outputs")
@ToString(of = {"id", "name", "gpioNumber", "creationDate", "signal", "pwmStatus"})
@EqualsAndHashCode(of = {"id"})
public class Output implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "gpio_number")
    private Integer gpioNumber;

    @Column(name = "signal")
    private Integer signal;

    @Column(name = "pwm_status")
    private Boolean pwmStatus;

    @Column(updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public Output() {
    }

    public Output(String name, Integer gpioNumber, Integer signal, Boolean pwmStatus) {
        this.name = name;
        this.gpioNumber = gpioNumber;
        this.signal = signal;
        this.pwmStatus = pwmStatus;
        this.creationDate = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGpioNumber() {
        return gpioNumber;
    }

    public void setGpioNumber(Integer gpioNumber) {
        this.gpioNumber = gpioNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getPwmStatus() {
        return pwmStatus;
    }

    public void setPwmStatus(Boolean pwmStatus) {
        this.pwmStatus = pwmStatus;
    }
}