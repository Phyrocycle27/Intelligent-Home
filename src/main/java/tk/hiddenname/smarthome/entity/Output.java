package tk.hiddenname.smarthome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "outputs")
@EqualsAndHashCode(of = {"outputId"}, callSuper = false)
public class Output extends ResourceSupport {

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer outputId;

    @Column
    private String name;

    @Column(name = "gpio")
    private Integer gpio;

    @Column
    private Boolean reverse;

    @Column(updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @Column
    private String type;

    public Output() {
    }

    public Output(Integer outputId, String name, Integer gpio, Boolean reverse,
                  LocalDateTime creationDate, String type) {
        this.outputId = outputId;
        this.name = name;
        this.gpio = gpio;
        this.reverse = reverse;
        this.creationDate = creationDate;
        this.type = type;
    }

    public Integer getOutputId() {
        return outputId;
    }

    public void setOutputId(Integer outputId) {
        this.outputId = outputId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGpio() {
        return gpio;
    }

    public void setGpio(Integer gpio) {
        this.gpio = gpio;
    }

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}