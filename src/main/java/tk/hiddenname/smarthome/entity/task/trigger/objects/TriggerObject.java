package tk.hiddenname.smarthome.entity.task.trigger.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerObjectGroup;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "TriggerObject")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class TriggerObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private TriggerObjectGroup group;
}
