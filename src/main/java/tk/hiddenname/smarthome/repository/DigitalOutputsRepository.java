package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entities.DigitalOutput;

@Repository
public interface DigitalOutputsRepository extends JpaRepository<DigitalOutput, Integer> {

}
