package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.model.hardware.Sensor;
import tk.hiddenname.smarthome.model.signal.SignalType;
import tk.hiddenname.smarthome.repository.SensorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SensorDatabaseService {

    private final SensorRepository repo;

    public List<Sensor> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public List<Sensor> getAllBySignalType(SignalType type) {
        return repo.findAllByGpioType(type);
    }

    public List<Sensor> getAllByAreaId(Long areaId) {
        return repo.findAllByAreaId(areaId);
    }

    public List<Sensor> getAllBySignalTypeAndAreaId(SignalType type, Long areaId) {
        return repo.findAllByGpioTypeAndAreaId(type, areaId);
    }

    public Sensor getOne(Long id) throws SensorNotFoundException {
        return repo.findById(id).orElseThrow(() -> new SensorNotFoundException(id));
    }

    public Sensor create(Sensor newDevice) {
        return repo.save(newDevice);
    }

    public Sensor update(Long id, Sensor newSensor) {
        return repo.findById(id)
                .map(sensor -> {
                    BeanUtils.copyProperties(newSensor, sensor, "id", "creationDate", "gpio");
                    return repo.save(sensor);
                }).orElseThrow(() -> new SensorNotFoundException(id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
