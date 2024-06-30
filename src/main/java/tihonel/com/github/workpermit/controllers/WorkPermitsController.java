package tihonel.com.github.workpermit.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tihonel.com.github.workpermit.dto.DtoPages;
import tihonel.com.github.workpermit.dto.DtoReport;
import tihonel.com.github.workpermit.dto.DtoWorkPermit;
import tihonel.com.github.workpermit.models.workpermit.Measure;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;
import tihonel.com.github.workpermit.services.*;
import tihonel.com.github.workpermit.utils.DtoReportValidator;
import tihonel.com.github.workpermit.utils.DtoWorkPermitValidator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/work_permits")
public class WorkPermitsController {
    private final DtoWorkPermitValidator dtoWorkPermitValidator;
    private final WorkPermitsService workPermitsService;
    private final TechnicsService technicsService;
    private final WorkersService workersService;
    private final PricesService pricesService;
    private static final String TARGET_WORK_PERMIT_XLS = "xlsFiles/";

    @Value("${workpermit.quantity_for_page}")
    private int workPermitsForPage;


    @Autowired
    public WorkPermitsController(DtoWorkPermitValidator dtoWorkPermitValidator, DtoReportValidator dtoReportValidator, WorkPermitsService workPermitsService, TechnicsService technicsService, WorkersService workersService, PricesService pricesService, ReportEntriesService reportEntriesService) {
        this.dtoWorkPermitValidator = dtoWorkPermitValidator;
        this.workPermitsService = workPermitsService;
        this.technicsService = technicsService;
        this.workersService = workersService;
        this.pricesService = pricesService;
    }

    @GetMapping
    public String showAll(Model model, @RequestParam("page") Optional<Integer> page) {
        if(page.isEmpty()){
            page = Optional.of(0);
        }
        List<WorkPermit> listWP = workPermitsService.findAll(page.get());
        model.addAttribute("listWP", listWP);
        model.addAttribute("dtoPages", new DtoPages(workPermitsService.quantityOfWorkPermits(), page.get(), workPermitsForPage));
        return "workpermit/all";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        DtoWorkPermit dtoWorkPermit = new DtoWorkPermit();
        List<Measure> measureList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            measureList.add(new Measure());
        }
        dtoWorkPermit.setMeasures(measureList);
        model.addAttribute("dtoWorkPermit", dtoWorkPermit);
        insertListsForSelect(model);
        return "workpermit/create";
    }

    @PostMapping
    public String saveNewWorkPermitAndCreateXls(@Valid DtoWorkPermit dtoWorkPermit, BindingResult bindingResult, Model model) {
        System.out.println("Метод сохранения");
        dtoWorkPermitValidator.validate(dtoWorkPermit, bindingResult);
        if (bindingResult.hasErrors()) {
            insertListsForSelect(model);
            return "workpermit/create";
        } else {
            workPermitsService.saveAndCreateXls(dtoWorkPermit);
            return "redirect:/work_permits";
        }
    }

    @GetMapping("/{id}")
    public String workPermitPage(@PathVariable("id") int id, Model model) {
        try {
            model.addAttribute(workPermitsService.getSimpleDtoById(id));
            model.addAttribute("dtoPrices", pricesService.findAll());
            model.addAttribute("dtoReport", new DtoReport());
        } catch (Exception e) {
            return "redirect:/work_permits";
        }
        return "workpermit/show";
    }

    @GetMapping("/{id}/download")
    public void downloadXlsFile(@PathVariable("id") int id, HttpServletResponse httpServletResponse){
        String fileName = workPermitsService.getXlsFileName(id);
        try (FileInputStream inputStream = new FileInputStream(TARGET_WORK_PERMIT_XLS + fileName + ".xls")) {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=workpermit.xls");
            IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int id, Model model) {
        DtoWorkPermit dtoWorkPermit = workPermitsService.getDtoById(id);
        model.addAttribute("dtoWorkPermit", dtoWorkPermit);
        insertListsForSelect(model);
        return "workpermit/update";
    }

    @PostMapping("/{id}/delete")
    public String deleteWorkPermit(@PathVariable("id") int id) {
        workPermitsService.deleteById(id);
        return "redirect:/work_permits";
    }

    @PostMapping("/{id}")
    public String updateWorkPermit(@PathVariable("id") int id, @Valid DtoWorkPermit dtoWorkPermit, BindingResult bindingResult, Model model) {
        dtoWorkPermitValidator.validate(dtoWorkPermit, bindingResult);
        if (bindingResult.hasErrors()) {
            insertListsForSelect(model);
            return "workpermit/update";
        }
        workPermitsService.saveAndCreateXls(dtoWorkPermit);
        return "redirect:/work_permits/" + id;
    }

    @GetMapping("/create_on_base/{id}")
    public String formForCreatNewBasedOnExisting(@PathVariable("id") int id, Model model){
        model.addAttribute(workPermitsService.getDtoForCreatingOnBase(id));
        insertListsForSelect(model);
        return "workpermit/create";
    }

    private void insertListsForSelect(Model model) {
        workersService.insertListsWithWorkers(model);

        model.addAttribute("technicsList", technicsService.findAll());
    }
}

