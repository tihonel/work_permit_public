package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.worker.Worker;

import java.util.List;

@Repository
public interface WorkersRepository extends JpaRepository<Worker, Integer> {
    List<Worker> findByAccessGroupGreaterThanEqual(int accessGroup);
    List<Worker> findWorkerByRightIssuingIsTrue();
    List<Worker> findWorkerByDriverIsTrue();
}
