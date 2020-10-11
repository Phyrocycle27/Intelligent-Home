package tk.hiddenname.smarthome.model.task.processing.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "processing_object")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "action", visible = true)
@JsonTypeIdResolver(ProcessingObjectTypeIdResolver.class)
public abstract class ProcessingObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ProcessingAction action;
}
