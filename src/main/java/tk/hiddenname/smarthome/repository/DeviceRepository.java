package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.signal.SignalType;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    List<Device> findAllByGpio_Type(SignalType type);

    List<Device> findAllByArea_Id(Integer areaId);

    List<Device> findAllByGpio_TypeAndArea_Id(SignalType type, Integer areaId);
}
