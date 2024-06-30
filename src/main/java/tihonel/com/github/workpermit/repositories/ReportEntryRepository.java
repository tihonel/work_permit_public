package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.ReportEntry;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportEntryRepository extends JpaRepository<ReportEntry, Integer> {
    List<ReportEntry> findAllByDateGreaterThanEqualAndDateLessThanEqual(LocalDate startDate, LocalDate endDate);
}
