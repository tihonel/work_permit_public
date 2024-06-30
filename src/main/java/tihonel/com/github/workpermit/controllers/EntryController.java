package tihonel.com.github.workpermit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EntryController {
    @GetMapping
    public String entry(){
        return "redirect:/work_permits";
    }
}
