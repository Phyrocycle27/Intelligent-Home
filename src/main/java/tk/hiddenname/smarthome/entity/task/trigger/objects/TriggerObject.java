package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.*;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "trigger_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(TriggerObjectTypeIdResolver.class)
public abstract class TriggerObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NonNull
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TriggerAction action;
}
