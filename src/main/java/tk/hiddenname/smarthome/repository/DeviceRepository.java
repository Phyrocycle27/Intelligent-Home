package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.Device;
import tk.hiddenname.smarthome.entity.GPIOType;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    List<Device> findByGpioType(GPIOType type);
}
