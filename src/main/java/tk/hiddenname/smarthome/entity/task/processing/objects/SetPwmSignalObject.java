package tk.hiddenname.smarthome.entity.task.processing.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "processing_set_pwm_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SetPwmSignalObject extends ProcessingObject {

    @NotNull
    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(nullable = false)
    private int delay = 0;

    @NotNull
    @Column(nullable = false)
    private int targetSignal;
}
