package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findByGpioType(@NotNull SignalType type);
}
