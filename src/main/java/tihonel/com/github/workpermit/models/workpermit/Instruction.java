package tihonel.com.github.workpermit.models.workpermit;

import jakarta.persistence.*;

@Entity
@Table(name = "instruction")
public class Instruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "string")
    private String string;
    @ManyToOne
    @JoinColumn(name = "work_permit_id", referencedColumnName = "id")
    private WorkPermit workPermit;

    public Instruction() {
    }

    public Instruction(String string, WorkPermit workPermit) {
        this.string = string;
        this.workPermit = workPermit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public WorkPermit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(WorkPermit workPermit) {
        this.workPermit = workPermit;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "id=" + id +
                ", string='" + string + '\'' +
                '}';
    }
}
