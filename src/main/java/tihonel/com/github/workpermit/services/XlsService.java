package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tihonel.com.github.workpermit.dao.DaoXls;
import tihonel.com.github.workpermit.docx.ReportToXls;
import tihonel.com.github.workpermit.docx.WorkPermitToXls;
import tihonel.com.github.workpermit.dto.DtoReportForTime;
import tihonel.com.github.workpermit.models.ReportEntry;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;
import tihonel.com.github.workpermit.repositories.ReportEntryRepository;
import tihonel.com.github.workpermit.repositories.WorkPermitsRepository;
import tihonel.com.github.workpermit.utils.CopyWbMaker;
import tihonel.com.github.workpermit.utils.ShaEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class XlsService {
    private final CopyWbMaker copyWbMaker;
    private final ShaEncoder shaEncoder;
    private final WorkPermitToXls workPermitToXls;
    private final ReportToXls reportToXls;
    private final DaoXls daoXls;
    private final WorkPermitsRepository workPermitsRepository;
    private final ReportEntryRepository reportEntryRepository;
    private final DateTimeFormatter dateFormatter;

    private final Logger logger = Logger.getLogger(XlsService.class.getName());

    private static final String TARGET_WORK_PERMIT_XLS = "xlsFiles";

    @Autowired
    public XlsService(CopyWbMaker copyWbMaker, ShaEncoder shaEncoder, WorkPermitToXls workPermitToXls, ReportToXls reportToXls, DaoXls daoXls, WorkPermitsRepository workPermitsRepository, ReportEntryRepository reportEntryRepository, DateTimeFormatter dateFormatter) {
        this.copyWbMaker = copyWbMaker;
        this.shaEncoder = shaEncoder;
        this.workPermitToXls = workPermitToXls;
        this.reportToXls = reportToXls;
        this.daoXls = daoXls;
        this.workPermitsRepository = workPermitsRepository;
        this.reportEntryRepository = reportEntryRepository;
        this.dateFormatter = dateFormatter;
    }

    @Transactional
    public String create(DtoReportForTime dtoReportForTime){
        String fileName = copyWbMaker.getTargetReportXlsFilename(dtoReportForTime);
        System.out.println("FILENAME FROM XLS SERVICE " + fileName);
        LocalDate startDate = LocalDate.parse(dtoReportForTime.startDate, dateFormatter);
        LocalDate endDate = LocalDate.parse(dtoReportForTime.endDate, dateFormatter);
        List<ReportEntry> entryList = reportEntryRepository.findAllByDateGreaterThanEqualAndDateLessThanEqual(startDate, endDate);
        reportToXls.createXlsFile(fileName, startDate, endDate, entryList);
        return fileName;
    }

    @Transactional
    public String create(WorkPermit workPermit) {
        String fileName = daoXls.findFileNameById(workPermit.getId());

        if(fileName == null){
            fileName = createNewXlsFile(workPermit);
        } else {
            if(!shaEncoder.checkSum(workPermit).equals(fileName)){
                new File(TARGET_WORK_PERMIT_XLS + fileName + ".xls").delete();
                daoXls.deleteNote(fileName);
                fileName = createNewXlsFile(workPermit);
            }
        }

        return fileName;
    }
    private String createNewXlsFile(WorkPermit workPermit) {
        String fileName;
        fileName = copyWbMaker.getTargetWorkPermitXlsFileName(workPermit);
        workPermitToXls.createXlsFile(workPermit, fileName);

        daoXls.saveFileName(cutFileName(fileName), workPermit.getId());
        return fileName;
    }

    @Transactional
    public String getXlsFileName(int id) {
        String fileNameById = daoXls.findFileNameById(id);
        if(fileNameById == null){
            logger.info("Файл не найден");
            return cutFileName(create(workPermitsRepository.findById(id).get()));
        }
        return fileNameById;
    }

    @Scheduled(cron = "@midnight")
    public void removeOldFiles(){
        try (Stream<Path> paths = Files.walk(Paths.get(TARGET_WORK_PERMIT_XLS + "/"))) {
            paths.forEach(x-> {
                File f = x.toFile();
                String fileName = f.getName();
                if(f.isFile() && !fileName.contains("report") && !fileName.contains("workpermit")){
                    f.delete();
                    daoXls.deleteNote(fileName);
                    logger.info("Файл " + fileName + "Удалён");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> fileNames = daoXls.findAll();
        if(!fileNames.isEmpty()){
            logger.info("Записей в БД без файлов " + fileNames.size());
        }
        fileNames.forEach(daoXls::deleteNote);
    }

    private String cutFileName(String fileName){
        return fileName.substring(TARGET_WORK_PERMIT_XLS.length() + 1, fileName.length() - 4);
    }
}
