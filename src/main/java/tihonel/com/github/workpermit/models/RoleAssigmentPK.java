package tihonel.com.github.workpermit.models;

import jakarta.persistence.*;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.io.Serializable;

@Embeddable
public class RoleAssigmentPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "work_permit_id")
    private WorkPermit workPermit;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private AbstractWorker worker;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public RoleAssigmentPK() {
    }

    public RoleAssigmentPK(WorkPermit workPermit, AbstractWorker worker, Role role) {
        this.workPermit = workPermit;
        this.worker = worker;
        this.role = role;
    }

    public WorkPermit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(WorkPermit workPermit) {
        this.workPermit = workPermit;
    }

    public AbstractWorker getWorker() {
        return worker;
    }

    public void setWorker(AbstractWorker worker) {
        this.worker = worker;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleAssigmentPK{" +
                ", worker=" + worker +
                ", role=" + role +
                '}';
    }
}
