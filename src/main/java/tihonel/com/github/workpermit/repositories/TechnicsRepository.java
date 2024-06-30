package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.Technic;

@Repository
public interface TechnicsRepository extends JpaRepository<Technic, Integer> {
}
