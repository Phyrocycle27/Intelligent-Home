package tk.hiddenname.smarthome.entity.task.processing.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tk.hiddenname.smarthome.service.task.impl.processor.Processor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "processing_set_digital_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class SetDigitalSignalObject extends ProcessingObject implements Processor {

    @NotNull
    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false)
    private int delay = 0;

    @NotNull
    @Column(nullable = false)
    private boolean targetState;
}
