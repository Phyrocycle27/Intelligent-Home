package tk.hiddenname.smarthome.entity.task.trigger;

import lombok.*;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "TriggerObjectGroup")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@RequiredArgsConstructor
public class TriggerObjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private TriggerAction action;

    @NonNull
    @OneToMany(mappedBy = "group")
    private List<TriggerObject> triggerObjects = new ArrayList<>();
}
