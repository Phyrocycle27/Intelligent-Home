package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.hardware.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
