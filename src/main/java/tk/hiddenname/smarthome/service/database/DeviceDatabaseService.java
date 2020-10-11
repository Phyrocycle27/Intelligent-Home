package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.exception.DeviceNotFoundException;
import tk.hiddenname.smarthome.model.hardware.Device;
import tk.hiddenname.smarthome.model.signal.SignalType;
import tk.hiddenname.smarthome.repository.DeviceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DeviceDatabaseService {

    private final DeviceRepository repo;

    public List<Device> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public List<Device> getAllBySignalType(SignalType type) {
        return repo.findAllByGpioType(type);
    }

    public List<Device> getAllByAreaId(Integer areaId) {
        return repo.findAllByAreaId(areaId);
    }

    public List<Device> getAllBySignalTypeAndAreaId(SignalType type, Integer areaId) {
        return repo.findAllByGpioTypeAndAreaId(type, areaId);
    }

    public Device getOne(Long id) throws DeviceNotFoundException {
        return repo.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public Device create(Device newDevice) {
        return repo.save(newDevice);
    }

    public Device update(Long id, Device newDevice) {
        return repo.findById(id)
                .map(device -> {
                    BeanUtils.copyProperties(newDevice, device, "id", "creationDate", "gpio");
                    return repo.save(device);
                }).orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
