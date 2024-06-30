package tihonel.com.github.workpermit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tihonel.com.github.workpermit.models.RoleAssigment;
import tihonel.com.github.workpermit.models.RoleAssigmentPK;

@Repository
public interface RoleAssigmentsRepository extends JpaRepository<RoleAssigment, RoleAssigmentPK> {
}
