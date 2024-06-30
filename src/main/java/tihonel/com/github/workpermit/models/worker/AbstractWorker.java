package tihonel.com.github.workpermit.models.worker;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import tihonel.com.github.workpermit.models.RoleAssigment;

import java.util.List;

@Entity
@Table(name = "worker")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class AbstractWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    @Column(name = "name")
    @NotEmpty(message = "Фамилия, инициалы не могут быть пустыми")
    @Size(min = 2, max = 24, message = "Количество символов должно быть от 2 до 24")
    protected String name;
    @NotEmpty(message = "Фамилия, инициалы не могут быть пустыми")
    @Size(min = 2, max = 24, message = "Количество символов должно быть от 2 до 24")
    @Column(name = "dative_Name")
    protected String dativeName;

    @Min(value = 0, message = "Группа допуска не может быть меньше 0")
    @Max(value = 5, message = "Группа допуска не может быть больше 5")
    @Column(name = "access_group")
    protected int accessGroup;
    @NotNull(message = "Указать наличие оперативных прав")
    @Column(name = "operational_rights")
    protected boolean operationalRights;
    @NotNull(message = "Необходимо выбрать есть право выдачи или нет")
    @Column(name = "right_issuing")
    protected boolean rightIssuing;
    @NotNull(message = "Необходимо выбрать может быть водителем или нет")
    @Column(name = "driver")
    protected boolean driver;

    @OneToMany(mappedBy = "roleAssigmentPK.worker")
    protected List<RoleAssigment> roleAssigments;

    public AbstractWorker() {
    }

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

    public String getDativeName() {
        return dativeName;
    }

    public void setDativeName(String dativeName) {
        this.dativeName = dativeName;
    }

    public int getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(int accessGroup) {
        this.accessGroup = accessGroup;
    }

    public boolean isRightIssuing() {
        return rightIssuing;
    }

    public void setRightIssuing(boolean rightIssuing) {
        this.rightIssuing = rightIssuing;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public List<RoleAssigment> getRoleAssigments() {
        return roleAssigments;
    }

    public void setRoleAssigments(List<RoleAssigment> roleAssigments) {
        this.roleAssigments = roleAssigments;
    }

    public boolean isOperationalRights() {
        return operationalRights;
    }

    public void setOperationalRights(boolean operationalRights) {
        this.operationalRights = operationalRights;
    }
}
