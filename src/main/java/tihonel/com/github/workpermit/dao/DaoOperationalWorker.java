package tihonel.com.github.workpermit.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;

import java.util.List;


@Component
public class DaoOperationalWorker {
    @PersistenceContext
    EntityManager entityManager;

    public DaoOperationalWorker() {
    }

    @Transactional
    public List<AbstractWorker> getOperationalWorker(){
        return entityManager.createQuery("from OperationalWorker", AbstractWorker.class).getResultList();
    }
}
