package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tk.hiddenname.smarthome.entities.Output;

import java.util.List;

public interface OutputsRepository extends JpaRepository<Output, Integer> {
    List<Output> findByPwmStatus(Boolean pwmStatus);
}
