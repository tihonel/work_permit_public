package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

@Repository
public interface WorkPermitsRepository extends JpaRepository<WorkPermit, Integer> {
}
