package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tihonel.com.github.workpermit.dto.DtoTechnicsList;
import tihonel.com.github.workpermit.models.Technic;
import tihonel.com.github.workpermit.repositories.TechnicsRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class TechnicsService {
    private final TechnicsRepository technicsRepository;

    @Autowired
    public TechnicsService(TechnicsRepository technicsRepository) {
        this.technicsRepository = technicsRepository;
    }

    public List<Technic> findAll(){
        List<Technic> technicList = technicsRepository.findAll();
        technicList.sort(Comparator.comparing(Technic::getName));
        return technicList;
    }

    public void save(Technic technic) {
        technicsRepository.save(technic);
    }

    public void updateAll(DtoTechnicsList dtoTechnicsList){
        technicsRepository.saveAll(dtoTechnicsList.getList());
    }

    public void delete(int id) {
        technicsRepository.deleteById(id);
    }

    public List<Technic> findAllById(List<Integer> ids){
        return technicsRepository.findAllById(ids);
    }
}
