package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "trigger_change_digital_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChangeDigitalSignalObject extends ChangeSignalObject {

    @Column(nullable = false)
    private boolean targetState;
}
