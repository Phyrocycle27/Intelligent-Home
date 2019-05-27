package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tk.hiddenname.smarthome.entities.Control;

public interface ControlRepository extends JpaRepository<Control, Integer> {
}
