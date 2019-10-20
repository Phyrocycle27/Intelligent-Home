package tk.hiddenname.smarthome.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.hiddenname.smarthome.entity.Output;

import java.util.List;

@Repository
public interface OutputsRepository extends JpaRepository<Output, Integer> {

    List<Output> findByType(String type, Sort sort);

    List<Output> findByType(String type);
}
