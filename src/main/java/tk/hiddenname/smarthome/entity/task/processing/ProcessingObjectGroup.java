package tk.hiddenname.smarthome.entity.task.processing;

import lombok.*;
import tk.hiddenname.smarthome.entity.task.processing.objects.ProcessingObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "ProcessingObjectGroup")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@RequiredArgsConstructor
public class ProcessingObjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private ProcessingAction action;

    @NonNull
    @OneToMany(mappedBy = "group")
    private List<ProcessingObject> processingObjects = new ArrayList<>();
}
