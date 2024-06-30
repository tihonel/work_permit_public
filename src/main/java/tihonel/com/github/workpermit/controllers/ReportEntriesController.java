package tihonel.com.github.workpermit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tihonel.com.github.workpermit.dto.DtoReport;
import tihonel.com.github.workpermit.services.PricesService;
import tihonel.com.github.workpermit.services.ReportEntriesService;
import tihonel.com.github.workpermit.services.WorkPermitsService;
import tihonel.com.github.workpermit.utils.DtoReportValidator;

import java.util.Optional;

@Controller
@RequestMapping("/report")
public class ReportEntriesController {
    private final PricesService pricesService;
    private final ReportEntriesService reportEntriesService;
    private final DtoReportValidator dtoReportValidator;
    private final WorkPermitsService workPermitsService;

    @Autowired
    public ReportEntriesController(PricesService pricesService, ReportEntriesService reportEntriesService, DtoReportValidator dtoReportValidator, WorkPermitsService workPermitsService) {
        this.pricesService = pricesService;
        this.reportEntriesService = reportEntriesService;
        this.dtoReportValidator = dtoReportValidator;
        this.workPermitsService = workPermitsService;
    }

    @PostMapping("/add/{id}")
    public String addReportEntryForWorkPermit(@PathVariable("id") int id, DtoReport dtoReport, Model model, BindingResult bindingResult){
        dtoReportValidator.validate(dtoReport, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute(workPermitsService.getSimpleDtoById(id));
            model.addAttribute("dtoPrices", pricesService.findAll());
            model.addAttribute("dtoReport", dtoReport);
            return "workpermit/show";
        }
        reportEntriesService.saveNewReportEntry(dtoReport, id);
        return "redirect:/work_permits/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteReportEntry(@PathVariable("id") int id){
        Optional<Integer> workPermitIdOptional = reportEntriesService.deleteById(id);
        if(workPermitIdOptional.isEmpty()){
            return "redirect:/work_permits";
        } else {
            int workPermitIt = workPermitIdOptional.get();
            return "redirect:/work_permits/" + workPermitIt;
        }
    }
}
