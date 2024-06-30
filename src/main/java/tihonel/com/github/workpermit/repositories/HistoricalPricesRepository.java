package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tihonel.com.github.workpermit.models.HistoricalPrice;
import tihonel.com.github.workpermit.models.Price;

import java.time.LocalDate;

public interface HistoricalPricesRepository extends JpaRepository<HistoricalPrice, Integer> {
    HistoricalPrice findHistoricalPriceByPriceIdAndEndDateIsNull(int id);

    @Query(value = "SELECT hp FROM HistoricalPrice hp WHERE hp.price.id = :priceId and hp.startDate <= :dayOfWork and (hp.endDate is null OR " +
            "hp.endDate >= :dayOfWork)")
    HistoricalPrice findHistoricalPriceByPriceIdAndDayOfWork(@Param("priceId") int priceId, @Param("dayOfWork") LocalDate dayOfWork);
}

