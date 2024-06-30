package tihonel.com.github.workpermit.models.workpermit;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="Measure")
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "work_permit_id", referencedColumnName = "id")
    private WorkPermit workPermit;
    @Column(name = "first_column")
    private String firstColumn;
    @Column(name = "second_column")
    private String secondColumn;
    @Column(name = "third_column")
    private String thirdColumn;

    public Measure() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public WorkPermit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(WorkPermit workPermit) {
        this.workPermit = workPermit;
    }

    public String getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(String firstColumn) {
        this.firstColumn = firstColumn;
    }

    public String getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(String secondColumn) {
        this.secondColumn = secondColumn;
    }

    public String getThirdColumn() {
        return thirdColumn;
    }

    public void setThirdColumn(String thirdColumn) {
        this.thirdColumn = thirdColumn;
    }

    public boolean isEmpty(){
        return (firstColumn == null || firstColumn.isEmpty()) &&
                (secondColumn == null || secondColumn.isEmpty()) &&
                (thirdColumn == null || thirdColumn.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measure measure = (Measure) o;

        if (id != measure.id) return false;
        if (!Objects.equals(workPermit, measure.workPermit)) return false;
        if (!Objects.equals(firstColumn, measure.firstColumn)) return false;
        if (!Objects.equals(secondColumn, measure.secondColumn))
            return false;
        return Objects.equals(thirdColumn, measure.thirdColumn);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (workPermit != null ? workPermit.hashCode() : 0);
        result = 31 * result + (firstColumn != null ? firstColumn.hashCode() : 0);
        result = 31 * result + (secondColumn != null ? secondColumn.hashCode() : 0);
        result = 31 * result + (thirdColumn != null ? thirdColumn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", firstColumn='" + firstColumn + '\'' +
                ", secondColumn='" + secondColumn + '\'' +
                ", thirdColumn='" + thirdColumn + '\'' +
                '}';
    }
}
