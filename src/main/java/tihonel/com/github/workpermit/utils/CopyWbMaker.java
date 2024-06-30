package tihonel.com.github.workpermit.utils;


import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tihonel.com.github.workpermit.dto.DtoReportForTime;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.io.*;

@Component
public class CopyWbMaker {
    private static final String ORIGIN_WORK_PERMIT_XLS = "static/workpermit.xls";
    private static final String ORIGIN_REPORT_XLS = "static/report.xls";
    private static final String TARGET_XLS = "xlsFiles/";
    private final ShaEncoder shaEncoder;
    @Autowired
    public CopyWbMaker(ShaEncoder shaEncoder) {
        this.shaEncoder = shaEncoder;
    }

    public String getTargetWorkPermitXlsFileName(WorkPermit workPermit){
        String fileName = shaEncoder.checkSum(workPermit);
        Workbook wb = getWorkBook(new File("xlsFiles/workpermit.xls"));
        fileName = TARGET_XLS + fileName + ".xls";
        saveWorkBook(wb, fileName);
        return fileName;
    }

    private Workbook getWorkBook(File origin){
        try {
            return WorkbookFactory.create(origin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveWorkBook(Workbook wb, String targetWorkPermitXls){
        try (FileOutputStream outputStream = new FileOutputStream(targetWorkPermitXls)){
            wb.write(outputStream);
            wb.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTargetReportXlsFilename(DtoReportForTime dtoReportForTime) {
        String fileName = shaEncoder.checkSum((dtoReportForTime.startDate + dtoReportForTime.endDate).getBytes());
        Workbook wb = getWorkBook(new File("xlsFiles/report.xls"));
        fileName = TARGET_XLS + fileName + ".xls";
        saveWorkBook(wb, fileName);
        return fileName;
    }

    private static void initDir(){
        File dir = new File("xlsFiles");
        dir.mkdir();
    }

    private void copyOriginReport(){
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(ORIGIN_REPORT_XLS);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("xlsFiles/report.xls"))) {

            outputStream.write(bis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyOriginWorkPermit(){
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(ORIGIN_WORK_PERMIT_XLS);
             BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("xlsFiles/workpermit.xls"))) {

            outputStream.write(bis.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void creatDirAndCopyFiles(){
        initDir();
        copyOriginReport();
        copyOriginWorkPermit();
    }
}
