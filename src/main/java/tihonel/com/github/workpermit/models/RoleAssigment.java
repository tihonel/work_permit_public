package tihonel.com.github.workpermit.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "role_assigment")
public class RoleAssigment {
    @EmbeddedId
    RoleAssigmentPK roleAssigmentPK;

    public RoleAssigment() {
    }

    public RoleAssigment(RoleAssigmentPK roleAssigmentPK) {
        this.roleAssigmentPK = roleAssigmentPK;
    }

    public RoleAssigmentPK getRoleAssigmentPK() {
        return roleAssigmentPK;
    }

    public void setRoleAssigmentPK(RoleAssigmentPK roleAssigmentPK) {
        this.roleAssigmentPK = roleAssigmentPK;
    }

    @Override
    public String toString() {
        return roleAssigmentPK.toString();
    }
}
