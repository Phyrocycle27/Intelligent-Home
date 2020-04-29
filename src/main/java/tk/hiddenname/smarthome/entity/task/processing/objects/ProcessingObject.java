package tk.hiddenname.smarthome.entity.task.processing.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingObjectGroup;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "ProcessingObject")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class ProcessingObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private ProcessingObjectGroup group;
}
