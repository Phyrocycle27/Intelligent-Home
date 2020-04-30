package tk.hiddenname.smarthome.entity.task.processing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "processing_group")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingObjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private ProcessingAction action;

    @JoinTable(
            name = "processing_group_to_object",
            joinColumns = @JoinColumn(name = "fk_group", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "fk_object", referencedColumnName = "id"))
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProcessingObject> processingObjects = new ArrayList<>();
}
