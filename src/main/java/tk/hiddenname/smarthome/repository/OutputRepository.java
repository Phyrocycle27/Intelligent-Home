package tk.hiddenname.smarthome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entities.Output;

@Repository
public interface OutputRepository extends JpaRepository<Output, Integer> {
}
