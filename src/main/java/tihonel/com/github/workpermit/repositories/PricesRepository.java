package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.Price;

import java.util.List;

@Repository
public interface PricesRepository extends JpaRepository<Price, Integer> {
    List<Price> findAllByDeletedFalse();
}
