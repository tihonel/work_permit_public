package tihonel.com.github.workpermit.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tihonel.com.github.workpermit.dto.DtoReportForTime;
import tihonel.com.github.workpermit.services.XlsService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/report")
public class ReportController {
    private final XlsService xlsService;

    @Autowired
    public ReportController(XlsService xlsService) {
        this.xlsService = xlsService;
    }

    @GetMapping
    public String reportPage(Model model){
        model.addAttribute("dtoReportForTime", new DtoReportForTime());
        return "report/show";
    }

    @PostMapping
    public void createAndDownloadReport(DtoReportForTime dtoReportForTime, HttpServletResponse httpServletResponse){
        String fileName = xlsService.create(dtoReportForTime);
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=report.xls");
            IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
