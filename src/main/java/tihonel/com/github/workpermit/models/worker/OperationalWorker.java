package tihonel.com.github.workpermit.models.worker;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "worker")
@DiscriminatorValue(value = "OW")
public class OperationalWorker extends AbstractWorker {
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
        return "OperationalWorker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dativeName='" + dativeName + '\'' +
                ", accessGroup=" + accessGroup +
                '}';
    }
}
