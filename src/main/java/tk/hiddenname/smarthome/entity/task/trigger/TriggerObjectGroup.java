package tk.hiddenname.smarthome.entity.task.trigger;

import lombok.*;
import tk.hiddenname.smarthome.entity.task.trigger.objects.TriggerObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "trigger_group")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class TriggerObjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private TriggerAction action;

    @JoinTable(
            name = "trigger_group_to_object",
            joinColumns = @JoinColumn(name = "fk_group", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_object", referencedColumnName = "id")
    )
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TriggerObject> triggerObjects = new ArrayList<>();
}
