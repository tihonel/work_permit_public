package tihonel.com.github.workpermit.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Price")
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "cost")
    private int cost;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(mappedBy = "price")
    private List<HistoricalPrice> historicalPrices;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<HistoricalPrice> getHistoricalPrices() {
        return historicalPrices;
    }

    public void setHistoricalPrices(List<HistoricalPrice> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", deleted=" + deleted +
                '}';
    }
}
