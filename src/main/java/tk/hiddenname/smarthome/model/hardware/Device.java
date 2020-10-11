package tk.hiddenname.smarthome.model.hardware;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "device")
@EqualsAndHashCode(of = {"id"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Size(min = 3, max = 25)
    @Column(nullable = false, length = 25)
    private String name;

    @Size(min = 3, max = 50)
    @Column(length = 50)
    private String description;

    @Column(nullable = false)
    private boolean reverse = false;

    @Column(nullable = false)
    private int areaId;

    @Column(nullable = false, updatable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @NotNull
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "type", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "gpioPin", column = @Column(nullable = false, updatable = false)),
            @AttributeOverride(name = "mode", column = @Column(nullable = false, updatable = false))
    })
    private GPIO gpio;
}
