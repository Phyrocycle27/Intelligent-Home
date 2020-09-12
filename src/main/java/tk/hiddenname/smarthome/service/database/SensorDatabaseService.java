package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.hardware.Sensor;
import tk.hiddenname.smarthome.entity.signal.SignalType;
import tk.hiddenname.smarthome.exception.SensorNotFoundException;
import tk.hiddenname.smarthome.repository.SensorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SensorDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(SensorDatabaseService.class);

    private final SensorRepository repo;

    public List<Sensor> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public List<Sensor> getAllBySignalType(SignalType type) {
        return repo.findByGpioType(type);
    }

    public Sensor getOne(Integer id) throws SensorNotFoundException {
        return repo.findById(id).orElseThrow(() -> {
            SensorNotFoundException e = new SensorNotFoundException(id);
            log.warn(e.getMessage());
            return e;
        });
    }

    public Sensor create(Sensor newDevice) {
        return repo.save(newDevice);
    }

    public Sensor update(Integer id, Sensor newSensor) {
        return repo.findById(id)
                .map(sensor -> {
                    BeanUtils.copyProperties(newSensor, sensor, "id", "creationDate", "gpio");
                    return repo.save(sensor);
                }).orElseThrow(() -> {
                    SensorNotFoundException e = new SensorNotFoundException(id);
                    log.warn(e.getMessage());
                    return e;
                });
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
