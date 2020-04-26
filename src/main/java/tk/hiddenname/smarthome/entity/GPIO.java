package tk.hiddenname.smarthome.entity;

import lombok.*;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "gpio")
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
@NoArgsConstructor
public class GPIO {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @Column(nullable = false, updatable = false)
    private Integer gpio;

    @NonNull
    @Column(nullable = false, updatable = false)
    private SignalType type;

    @NonNull
    @Column(nullable = false, updatable = false)
    private GPIOMode mode;
}