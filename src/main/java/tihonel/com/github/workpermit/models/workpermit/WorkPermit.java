package tihonel.com.github.workpermit.models.workpermit;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import tihonel.com.github.workpermit.models.ReportEntry;
import tihonel.com.github.workpermit.models.Role;
import tihonel.com.github.workpermit.models.RoleAssigment;
import tihonel.com.github.workpermit.models.Technic;
import tihonel.com.github.workpermit.models.worker.Worker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "work_permit")
public class WorkPermit {
    @Transient
    private Map<Role, Worker> roleWorkerMap;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
//    @Column(name = "organization")
//    private String organization;
//    @Column(name = "department")
//    private String department;
    @Column(name = "number")
    private String number;

    @OneToMany(mappedBy = "roleAssigmentPK.workPermit", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RoleAssigment> roleAssigments;

    @OneToMany(mappedBy = "workPermit", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Task> task;

    @Column(name = "short_name")
    private String shortName;

    @OneToMany(mappedBy = "workPermit", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Measure> measures;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDateTime;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endDateTime;

    @Column(name = "issuing_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime issuingDateTime;

    @OneToMany(mappedBy = "workPermit", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Instruction> instructions;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "work_permit_technic",
            joinColumns = @JoinColumn(name = "work_permit_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "technic_id", referencedColumnName = "id"))
    private List<Technic> technicList;

    @OneToMany(mappedBy = "workPermit")
    private List<ReportEntry> reportEntries;

    public WorkPermit() {
    }

    public WorkPermit(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<RoleAssigment> getRoleAssigments() {
        return roleAssigments;
    }

    public void setRoleAssigments(List<RoleAssigment> roleAssigments) {
        this.roleAssigments = roleAssigments;
    }

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public Map<Role, Worker> getRoleWorkerMap() {
        return roleWorkerMap;
    }

    public void setRoleWorkerMap(Map<Role, Worker> roleWorkerMap) {
        this.roleWorkerMap = roleWorkerMap;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getIssuingDateTime() {
        return issuingDateTime;
    }

    public void setIssuingDateTime(LocalDateTime issuingDateTime) {
        this.issuingDateTime = issuingDateTime;
    }

    public List<Technic> getTechnicList() {
        return technicList;
    }

    public void setTechnicList(List<Technic> technicList) {
        this.technicList = technicList;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public List<ReportEntry> getReportEntries() {
        return reportEntries;
    }

    public void setReportEntries(List<ReportEntry> reportEntries) {
        this.reportEntries = reportEntries;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "WorkPermit{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", roleAssigments=" + roleAssigments +
                ", task=" + task +
                ", measures=" + measures +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", issuingDateTime=" + issuingDateTime +
                ", instructions=" + instructions +
                ", technicList=" + technicList +
                '}';
    }
}
