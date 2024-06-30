package tihonel.com.github.workpermit.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tihonel.com.github.workpermit.dto.DtoPricesList;
import tihonel.com.github.workpermit.models.Price;
import tihonel.com.github.workpermit.services.PricesService;

@Controller
@RequestMapping("/prices")
public class PricesController {
    private final PricesService pricesService;

    @Autowired
    public PricesController(PricesService pricesService) {
        this.pricesService = pricesService;
    }

    @GetMapping
    public String showAll(Model model, @ModelAttribute("emptyPrice") Price price, HttpServletRequest request){
        model.addAttribute("dto", pricesService.findAll());
        return "prices/all";
    }

    @PostMapping
    public String save(@ModelAttribute("emptyPrice") Price price){
        pricesService.save(price);
        return "redirect:/prices";
    }

    @PostMapping("/save_all")
    public String updateAll(@ModelAttribute("dto") DtoPricesList dtoPricesList){
        if(dtoPricesList.getPriceList() == null || dtoPricesList.getPriceList().isEmpty()){
            return "redirect:/prices";
        }
        pricesService.updateAll(dtoPricesList);
        return "redirect:/prices";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/delete")
    public void delete(@PathVariable("id") int id){
        pricesService.delete(id);
    }
}
