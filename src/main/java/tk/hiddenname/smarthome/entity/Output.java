package tk.hiddenname.smarthome.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "outputs")
@EqualsAndHashCode(of = {"outputId"}, callSuper = false)
public class Output {

    @Id
    @Column(name = "id")
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
}