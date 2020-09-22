package tk.hiddenname.smarthome.service.database;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tk.hiddenname.smarthome.entity.Area;
import tk.hiddenname.smarthome.repository.AreaRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AreaDatabaseService {

    private final AreaRepository repo;

    public List<Area> getAll() {
        return repo.findAll(Sort.by("id"));
    }

}
