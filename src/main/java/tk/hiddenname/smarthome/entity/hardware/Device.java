package tk.hiddenname.smarthome.entity.hardware;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "device")
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean reverse;

    @Column(nullable = false, updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "type", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "gpio", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "mode", column = @Column(nullable = false, updatable = false))
    })
    private GPIO gpio;
}
