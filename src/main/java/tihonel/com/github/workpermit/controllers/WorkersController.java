package tihonel.com.github.workpermit.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tihonel.com.github.workpermit.models.worker.Worker;
import tihonel.com.github.workpermit.services.WorkersService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/workers")
public class WorkersController {
    private final WorkersService workersService;

    @Autowired
    public WorkersController(WorkersService workersService) {
        this.workersService = workersService;
    }

    @GetMapping
    public String showAll(Model model){
        model.addAttribute("workers", workersService.findAll());
        return "workers/all";
    }

    @GetMapping("/create")
    public String createForm(@ModelAttribute("worker") Worker worker){
        return "workers/create";
    }

    @PostMapping
    public String saveNewWorker(@Valid @ModelAttribute("worker") Worker worker, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "workers/create";
        } else {
            workersService.save(worker);
            return "redirect:/workers";
        }
    }

    @GetMapping("/{id}")
    public String workerPage(@PathVariable("id") int id, Model model){
        try {
            model.addAttribute("worker", workersService.findWorkerById(id));
        } catch (NoSuchElementException e) {
            return "redirect:/workers";
        }
        return "workers/worker";
    }

    @GetMapping("/{id}/update")
    public String updatePage(@PathVariable("id") int id, Model model){
        try {
            model.addAttribute("worker", workersService.findWorkerById(id));
        } catch (NoSuchElementException e) {
            return "redirect:/workers";
        }
        return "workers/update";
    }

    @PostMapping("/{id}")
    public String updateWorker(@PathVariable("id") int id, @Valid @ModelAttribute Worker worker, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "workers/update";
        } else {
            worker.setId(id);
            workersService.save(worker);
            return "redirect:/workers/" + id;
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteWorker(@PathVariable("id") int id){
        workersService.deleteWorker(id);
        return "redirect:/workers";
    }
}
