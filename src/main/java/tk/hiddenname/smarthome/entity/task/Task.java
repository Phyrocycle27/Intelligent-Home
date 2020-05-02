package tk.hiddenname.smarthome.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingGroup;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerGroup;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "task")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_to_trigger_group",
            joinColumns = @JoinColumn(name = "fk_task", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_group", referencedColumnName = "id"))
    @MapKey(name = "action")
    private Map<TriggerAction, TriggerGroup> triggerGroups = new HashMap<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_to_processing_group",
            joinColumns = @JoinColumn(name = "fk_task", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_group", referencedColumnName = "id"))
    @MapKey(name = "action")
    private Map<ProcessingAction, ProcessingGroup> processingGroups = new HashMap<>();
}
