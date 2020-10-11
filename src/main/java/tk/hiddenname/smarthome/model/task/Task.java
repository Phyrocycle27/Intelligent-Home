package tk.hiddenname.smarthome.model.task;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import tk.hiddenname.smarthome.model.task.processing.objects.ProcessingObject;
import tk.hiddenname.smarthome.model.task.trigger.objects.TriggerObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "task")
@EqualsAndHashCode(of = {"id"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String description;

    @NotNull
    @JoinTable(
            name = "task_to_trigger_object",
            joinColumns = @JoinColumn(name = "fk_task", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_object", referencedColumnName = "id")
    )
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<TriggerObject> triggerObjects = new HashSet<>();

    @NotNull
    @JoinTable(
            name = "task_to_processing_object",
            joinColumns = @JoinColumn(name = "fk_task", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_object", referencedColumnName = "id"))
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<ProcessingObject> processingObjects = new HashSet<>();
}
