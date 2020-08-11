package tk.hiddenname.smarthome.entity.task.processing.objects;

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
@Table(name = "processing_set_digital_signal")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SetDigitalSignalObject extends SetSignalObject {

    @Column(nullable = false)
    private boolean targetState;
}