package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import tihonel.com.github.workpermit.dao.DaoOperationalWorker;
import tihonel.com.github.workpermit.dao.DaoUnassignedWorker;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;
import tihonel.com.github.workpermit.models.worker.Worker;
import tihonel.com.github.workpermit.repositories.RoleAssigmentsRepository;
import tihonel.com.github.workpermit.repositories.WorkersRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WorkersService {
    private final WorkersRepository workersRepository;
    private final RoleAssigmentsRepository roleAssigmentsRepository;

    private final DaoOperationalWorker daoOperationalWorker;
    private final DaoUnassignedWorker daoUnassignedWorker;

    @Autowired
    public WorkersService(WorkersRepository workersRepository, RoleAssigmentsRepository roleAssigmentsRepository, DaoOperationalWorker daoOperationalWorker, DaoUnassignedWorker unassignedWorker) {
        this.workersRepository = workersRepository;
        this.roleAssigmentsRepository = roleAssigmentsRepository;
        this.daoOperationalWorker = daoOperationalWorker;
        this.daoUnassignedWorker = unassignedWorker;
    }
    @Transactional
    public void save(Worker worker){
        workersRepository.save(worker);
    }

    public List<Worker> findAll(){
        return workersRepository.findAll(Sort.by("accessGroup").descending());
    }

    public Worker findWorkerById(int id){
        return workersRepository.findById(id).orElseThrow();
    }
    @Transactional
    public void deleteWorker(int id) {
        workersRepository.deleteById(id);
    }

    public AbstractWorker findAbstractWorkerById(int id){
        Optional<AbstractWorker> abstractWorker = daoOperationalWorker.getOperationalWorker().stream().findFirst();
        if(abstractWorker.isPresent()){
            if(abstractWorker.get().getId() == id){
                return abstractWorker.get();
            }
        }
        abstractWorker = daoUnassignedWorker.getUnassignedWorker().stream().findFirst();
        if(abstractWorker.isPresent()){
            if(abstractWorker.get().getId() == id){
                return abstractWorker.get();
            }
        }
        return findWorkerById(id);
    }

    public void insertListsWithWorkers(Model model) {
        List<Worker> allWorkers = findAll();

        List<AbstractWorker> responsibleList = daoUnassignedWorker.getUnassignedWorker();
        List<AbstractWorker> admittingList = daoOperationalWorker.getOperationalWorker();
        List<AbstractWorker> producerList = daoUnassignedWorker.getUnassignedWorker();
        List<AbstractWorker> issuingList = new ArrayList<>();
        List<AbstractWorker> members = new ArrayList<>();
        List<AbstractWorker> observerList = daoUnassignedWorker.getUnassignedWorker();

        for(Worker w : allWorkers){
            if(w.getAccessGroup() >= 5){
                responsibleList.add(w);
            }
            if(w.getAccessGroup() >= 4 && w.isOperationalRights()){
                admittingList.add(w);
            }
            if(w.getAccessGroup() >= 4){
                producerList.add(w);
                observerList.add(w);
            }
            if(w.isRightIssuing()){
                issuingList.add(w);
            }
            members.add(w);
        }

        model.addAttribute("responsibleList", responsibleList);
        model.addAttribute("admittingList", admittingList);
        model.addAttribute("producerList", producerList);
        model.addAttribute("issuingList", issuingList);
        model.addAttribute("members", members);
        model.addAttribute("observerList", observerList);
    }
}
