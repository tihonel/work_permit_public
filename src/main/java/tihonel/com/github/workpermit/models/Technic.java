package tihonel.com.github.workpermit.models;

import jakarta.persistence.*;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.util.List;

@Entity
@Table(name = "technic")
public class Technic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "technicList", cascade = CascadeType.PERSIST)
    private List<WorkPermit> workPermit;

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

    public List<WorkPermit> getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(List<WorkPermit> workPermit) {
        this.workPermit = workPermit;
    }

    @Override
    public String toString() {
        return "Technic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
