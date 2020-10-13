package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.model.Area;
import tk.hiddenname.smarthome.repository.AreaRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AreaDatabaseService {

    private final AreaRepository repo;

    public List<Area> getAll() {
        return repo.findAll(Sort.by("id"));
    }

    public Area getOne(Long id) {
        return repo.getOne(id);
    }

    public Area create(Area newArea) {
        return repo.save(newArea);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
