package tihonel.com.github.workpermit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tihonel.com.github.workpermit.dto.DtoTechnicsList;
import tihonel.com.github.workpermit.models.Technic;
import tihonel.com.github.workpermit.services.TechnicsService;

@Controller
@RequestMapping("/technics")
public class TechnicsController {
    private final TechnicsService technicsService;

    @Autowired
    public TechnicsController(TechnicsService technicsService) {
        this.technicsService = technicsService;
    }

    @GetMapping
    public String showAll(Model model, @ModelAttribute("emptyTechnic") Technic technic){
        DtoTechnicsList dtoTechnicsList = new DtoTechnicsList(technicsService.findAll());
        model.addAttribute("dto", dtoTechnicsList);
        return "technics/all";
    }

    @PostMapping
    public String save(@ModelAttribute Technic technic){
        technicsService.save(technic);
        return "redirect:/technics";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/delete")
    public void delete(@PathVariable("id") int id){
        technicsService.delete(id);
    }

    @PostMapping("/save_all")
    public String updateAll(@ModelAttribute("dto") DtoTechnicsList dtoTechnicsList){
        if(dtoTechnicsList.getList() == null || dtoTechnicsList.getList().isEmpty()){
            return "redirect:/technics";
        }
        technicsService.updateAll(dtoTechnicsList);
        return "redirect:/technics";
    }
}
