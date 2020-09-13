package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.hardware.Device;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.repository.DeviceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DeviceDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(DeviceDatabaseService.class);

    private final DeviceRepository repo;

    public List<Device> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public List<Device> getAllBySignalType(SignalType type) {
        return repo.findAllByGpio_Type(type);
    }

    public List<Device> getAllByAreaId(Integer areaId) {
        return repo.findAllByArea_Id(areaId);
    }

    public List<Device> getAllBySignalTypeAndAreaId(SignalType type, Integer areaId) {
        return repo.findAllByGpio_TypeAndArea_Id(type, areaId);
    }

    public Device getOne(Integer id) throws DeviceNotFoundException {
        return repo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public Device create(Device newDevice) {
        return repo.save(newDevice);
    }

    public Device update(Integer id, Device newDevice) {
        return repo.findById(id)
                .map(device -> {
                    BeanUtils.copyProperties(newDevice, device, "id", "creationDate", "gpio");
                    return repo.save(device);
                }).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
