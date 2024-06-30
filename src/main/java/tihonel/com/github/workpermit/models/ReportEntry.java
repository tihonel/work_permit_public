package tihonel.com.github.workpermit.models;

import jakarta.persistence.*;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.time.LocalDate;

@Entity
@Table(name = "report_entry")
public class ReportEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_work")
    private LocalDate date;
    @Column(name = "quantity")
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "work_permit_id", referencedColumnName = "id")
    private WorkPermit workPermit;
    @ManyToOne
    @JoinColumn(name = "historical_price_id", referencedColumnName = "id")
    private HistoricalPrice historicalPrice;

    public ReportEntry() {
    }

    public ReportEntry(LocalDate date, int quantity, WorkPermit workPermit, HistoricalPrice historicalPrice) {
        this.date = date;
        this.quantity = quantity;
        this.workPermit = workPermit;
        this.historicalPrice = historicalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public WorkPermit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(WorkPermit workPermit) {
        this.workPermit = workPermit;
    }

    public HistoricalPrice getHistoricalPrice() {
        return historicalPrice;
    }

    public void setHistoricalPrice(HistoricalPrice historicalPrice) {
        this.historicalPrice = historicalPrice;
    }
}
