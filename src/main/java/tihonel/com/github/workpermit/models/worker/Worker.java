package tihonel.com.github.workpermit.models.worker;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "worker")
@DiscriminatorValue("W")
public class Worker extends AbstractWorker {
    public Worker() {
    }

    public Worker(String name, String dativeName, int accessGroup) {
        this.name = name;
        this.dativeName = dativeName;
        this.accessGroup = accessGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (id != worker.id) return false;
        if (accessGroup != worker.accessGroup) return false;
        if (rightIssuing != worker.rightIssuing) return false;
        if (driver != worker.driver) return false;
        if (!Objects.equals(name, worker.name)) return false;
        return Objects.equals(dativeName, worker.dativeName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (dativeName != null ? dativeName.hashCode() : 0);
        result = 31 * result + accessGroup;
        result = 31 * result + (rightIssuing ? 1 : 0);
        result = 31 * result + (driver ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dativeName='" + dativeName + '\'' +
                ", accessGroup=" + accessGroup +
                '}';
    }
}
