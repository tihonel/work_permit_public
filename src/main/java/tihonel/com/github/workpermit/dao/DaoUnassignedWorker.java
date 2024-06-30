package tihonel.com.github.workpermit.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;

import java.util.List;

@Component
public class DaoUnassignedWorker {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<AbstractWorker> getUnassignedWorker(){
        return entityManager.createQuery("from UnassignedWorker", AbstractWorker.class).getResultList();
    }
}
