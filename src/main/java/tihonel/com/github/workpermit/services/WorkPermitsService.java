package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tihonel.com.github.workpermit.dto.DtoWorkPermit;
import tihonel.com.github.workpermit.dto.SimpleDtoWorkPermit;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;
import tihonel.com.github.workpermit.repositories.RoleAssigmentsRepository;
import tihonel.com.github.workpermit.repositories.WorkPermitsRepository;
import tihonel.com.github.workpermit.utils.DtoWorkPermitMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WorkPermitsService {
    private final WorkPermitsRepository workPermitsRepository;
    private final RoleAssigmentsRepository roleAssigmentsRepository;
    private final DtoWorkPermitMapper dtoWorkPermitMapper;
    private final XlsService xlsService;
    @Value("${workpermit.quantity_for_page}")
    private int workpermitsForPage;

    @Autowired
    public WorkPermitsService(WorkPermitsRepository workPermitsRepository, RoleAssigmentsRepository roleAssigmentsRepository, DtoWorkPermitMapper dtoWorkPermitMapper, XlsService xlsService) {
        this.workPermitsRepository = workPermitsRepository;
        this.roleAssigmentsRepository = roleAssigmentsRepository;
        this.dtoWorkPermitMapper = dtoWorkPermitMapper;
        this.xlsService = xlsService;
    }

    public List<WorkPermit> findAll(int page){
        return workPermitsRepository.findAll(PageRequest.of(page, workpermitsForPage, Sort.by("issuingDateTime", "number").descending())).getContent();
    }

    public long quantityOfWorkPermits(){
        return workPermitsRepository.count();
    }

    public SimpleDtoWorkPermit getSimpleDtoById(int id){
        return dtoWorkPermitMapper.getSimpleDtoWorkPermit(findById(id));
    }

    public WorkPermit findById(int id){
        WorkPermit workPermit = workPermitsRepository.findById(id).orElseThrow();
        return workPermit;
    }

    @Transactional
    public void deleteById(int id){
        workPermitsRepository.deleteById(id);
    }

    @Transactional
    public void saveAndCreateXls(DtoWorkPermit dtoWorkPermit){
        WorkPermit workPermit = dtoWorkPermitMapper.createWorkPermitFromDto(dtoWorkPermit);
        workPermitsRepository.save(workPermit);
        workPermit.getId();
        System.out.println(xlsService.create(workPermit));
    }

    public DtoWorkPermit getDtoById(int id) {
        WorkPermit workPermit = findById(id);
        return dtoWorkPermitMapper.getDtoWorkPermit(workPermit);
    }

    public DtoWorkPermit getDtoForCreatingOnBase(int id) {
        DtoWorkPermit dtoWorkPermit = getDtoById(id);
        dtoWorkPermit.setNumber(null);
        dtoWorkPermit.setId(0);
        return dtoWorkPermit;
    }
    @Transactional
    public String getXlsFileName(int id){
        return xlsService.getXlsFileName(id);
    }
}
