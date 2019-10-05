package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.output.PwmOutput;

@Repository
public interface PwmOutputsRepository extends JpaRepository<PwmOutput, Integer> {

}
