package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
}
