package tihonel.com.github.workpermit.docx;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tihonel.com.github.workpermit.dto.DtoReportForTime;
import tihonel.com.github.workpermit.models.ReportEntry;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class ReportToXls {
    @Value("${report.name.row}")
    private int nameRow;
    @Value("${report.name.cell}")
    private int nameCell;
    @Value("${report.table.startRow}")
    private int startRow;
    @Value("${report.table.dateColumn}")
    private int dateColumn;
    @Value("${report.table.nameOfObjectColumn}")
    private int nameOfObjectColumn;
    @Value("${report.table.nameOfPriceColumn}")
    private int nameOfPriceColumn;
    @Value("${report.table.unitOfMeasurementColumn}")
    private int unitOfMeasurementColumn;
    @Value("${report.table.quantityColumn}")
    private int quantityColumn;
    @Value("${report.table.numberColumn}")
    private int numberColumn;
    @Value("${report.table.valueOfPriceColumn}")
    private int valueOfPriceColumn;

    private final DateTimeFormatter dateFormatterForReport;

    @Autowired
    public ReportToXls(DateTimeFormatter dateFormatterForReport) {
        this.dateFormatterForReport = dateFormatterForReport;
    }

    public void createXlsFile(String fileName, LocalDate startDate, LocalDate endDate, List<ReportEntry> entryList) {
        try (FileInputStream inputStream = new FileInputStream(fileName);
             Workbook workbook = WorkbookFactory.create(inputStream);
             FileOutputStream outputStream = new FileOutputStream(fileName)) {

            Sheet sheet = workbook.getSheet("sheet");

            fillName(startDate, endDate, sheet);

            fillRows(entryList, sheet);

            workbook.write(outputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void fillName(LocalDate startDate, LocalDate endDate, Sheet sheet) {
        StringBuilder name = new StringBuilder("Отчет  об объеме выполненных работ ЭТЛ  за ");

        if(startDate.getYear() == endDate.getYear() && startDate.getMonth().equals(endDate.getMonth())){
            name.append(startDate.getMonth())
                    .append(" ")
                    .append(startDate.getYear())
                    .append(" года");
        } else {
            name.append(startDate.getMonth())
                    .append(" ")
                    .append(startDate.getYear())
                    .append(" года - ")
                    .append(endDate.getMonth())
                    .append(" ")
                    .append(endDate.getYear())
                    .append(" года");
        }
        Row row = sheet.getRow(nameRow);
        Cell cell = row.getCell(nameCell);
        cell.setCellValue(name.toString());
    }

    private void fillRows(List<ReportEntry> entryList, Sheet sheet) {
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();

        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Time New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(Font.COLOR_NORMAL);
        font.setBold(false);
        cellStyle.setFont(font);

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        BorderStyle borderStyle = BorderStyle.THIN;
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderBottom(borderStyle);

        int i = 0;
        int summaryPrice = 0;
        for(ReportEntry entry : entryList){
            Row row = sheet.getRow(startRow + i);

            Cell date = row.getCell(dateColumn);
            Cell nameOfObject = row.getCell(nameOfObjectColumn);
            Cell nameOfPrice = row.getCell(nameOfPriceColumn);
            Cell unitOfMeasurement = row.getCell(unitOfMeasurementColumn);
            Cell quantity = row.getCell(quantityColumn);
            Cell number = row.getCell(numberColumn);
            Cell valueOfPrice = row.getCell(valueOfPriceColumn);

            date.setCellStyle(cellStyle);
            nameOfObject.setCellStyle(cellStyle);
            nameOfPrice.setCellStyle(cellStyle);
            unitOfMeasurement.setCellStyle(cellStyle);
            quantity.setCellStyle(cellStyle);
            number.setCellStyle(cellStyle);
            valueOfPrice.setCellStyle(cellStyle);

            date.setCellType(CellType.STRING);
            date.setCellValue(entry.getDate().format(dateFormatterForReport));

            nameOfObject.setCellValue(entry.getWorkPermit().getShortName());
            nameOfPrice.setCellValue(entry.getHistoricalPrice().getPrice().getName());
            unitOfMeasurement.setCellValue("шт");
            quantity.setCellValue(entry.getQuantity());
            number.setCellValue(entry.getWorkPermit().getNumber());

            int price = entry.getHistoricalPrice().getCost() * entry.getQuantity();
            valueOfPrice.setCellValue(price);
            summaryPrice +=price;

            i++;
        }

        Row row = sheet.getRow(startRow + i);
        Cell summary = row.getCell(dateColumn);
        summary.setCellStyle(cellStyle);
        Cell summaryValue = row.getCell(valueOfPriceColumn);
        summaryValue.setCellStyle(cellStyle);
        summary.setCellType(CellType.STRING);
        summary.setCellValue("Итого:");
        summaryValue.setCellValue(summaryPrice);
    }
}
