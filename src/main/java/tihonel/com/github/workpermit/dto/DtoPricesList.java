package tihonel.com.github.workpermit.dto;

import tihonel.com.github.workpermit.models.Price;

import java.util.List;

public class DtoPricesList {
    List<Price> priceList;

    public DtoPricesList() {
    }

    public DtoPricesList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }
}
