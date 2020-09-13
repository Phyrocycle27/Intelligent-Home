package tk.hiddenname.smarthome.entity.hardware;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import tk.hiddenname.smarthome.entity.Area;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sensor")
@EqualsAndHashCode(of = {"id"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean reverse;

    @Column(updatable = false, nullable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "type", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "gpio", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "mode", column = @Column(nullable = false, updatable = false))
    })
    private GPIO gpio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order")
    private Area area;
}
