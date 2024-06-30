package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tihonel.com.github.workpermit.dto.DtoPricesList;
import tihonel.com.github.workpermit.models.HistoricalPrice;
import tihonel.com.github.workpermit.models.Price;
import tihonel.com.github.workpermit.repositories.HistoricalPricesRepository;
import tihonel.com.github.workpermit.repositories.PricesRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PricesService {
    private final PricesRepository pricesRepository;
    private final HistoricalPricesRepository historicalPricesRepository;

    @Autowired
    public PricesService(PricesRepository pricesRepository, HistoricalPricesRepository historicalPricesRepository) {
        this.pricesRepository = pricesRepository;
        this.historicalPricesRepository = historicalPricesRepository;
    }

    public DtoPricesList findAll() {
        List<Price> priceList = pricesRepository.findAllByDeletedFalse();
        priceList.sort((x, y) -> {
            return y.getCost() - x.getCost();
        });
        DtoPricesList dtoPricesList = new DtoPricesList(priceList);
        return dtoPricesList;
    }

    @Transactional
    public void save(Price price) {
        pricesRepository.save(price);
        saveNewHistoricalPrice(price);
    }

    @Transactional
    public void updateAll(DtoPricesList dtoPricesList) {
        System.out.println("Обновление");
        List<Price> actualPrices = pricesRepository.findAllByDeletedFalse();
        List<Price> updatedPrices = dtoPricesList.getPriceList();

        actualPrices.sort(Comparator.comparingInt(Price::getId));
        updatedPrices.sort(Comparator.comparingInt(Price::getId));

        Iterator<Price> actualIterator = actualPrices.iterator();
        Iterator<Price> updatedIterator = updatedPrices.iterator();

        while (updatedIterator.hasNext()) {
            Price actualPrice = actualIterator.next();
            Price updatedPrice = updatedIterator.next();
            if (actualPrice.getId() == updatedPrice.getId() && updatedPrice.getCost() == actualPrice.getCost()) {
                updatedIterator.remove();
            } else if (actualPrice.getId() == updatedPrice.getId() && updatedPrice.getCost() != actualPrice.getCost()) {
                actualPrice.setCost(updatedPrice.getCost());
            }
        }
        updateHistoricalPrices(updatedPrices);
    }

    @Transactional
    void saveNewHistoricalPrice(Price price){
        HistoricalPrice newHistoricalPrice = new HistoricalPrice();
        newHistoricalPrice.setPrice(price);
        newHistoricalPrice.setCost(price.getCost());
        newHistoricalPrice.setStartDate(LocalDate.now());
        historicalPricesRepository.save(newHistoricalPrice);
    }

    @Transactional
    void updateHistoricalPrices(List<Price> updatedPrices){
        for(Price price : updatedPrices){
            HistoricalPrice prevHistoricalPrice = historicalPricesRepository.findHistoricalPriceByPriceIdAndEndDateIsNull(price.getId());
            LocalDate now = LocalDate.now();
            if(prevHistoricalPrice.getStartDate().equals(now)){
               prevHistoricalPrice.setCost(price.getCost());
            } else {
                prevHistoricalPrice.setEndDate(now);
                saveNewHistoricalPrice(price);
            }
        }
    }

    @Transactional
    public void delete(int id) {
        Optional<Price> optionalPrice = pricesRepository.findById(id);
        if (optionalPrice.isPresent()) {
            Price price = optionalPrice.get();
            price.setDeleted(true);
            pricesRepository.save(price);
        }
    }
}
