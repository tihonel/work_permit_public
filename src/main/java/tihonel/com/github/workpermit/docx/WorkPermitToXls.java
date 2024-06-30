package tihonel.com.github.workpermit.docx;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import tihonel.com.github.workpermit.models.*;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;
import tihonel.com.github.workpermit.models.worker.Worker;
import tihonel.com.github.workpermit.models.workpermit.Instruction;
import tihonel.com.github.workpermit.models.workpermit.Measure;
import tihonel.com.github.workpermit.models.workpermit.Task;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
@PropertySource("classpath:insertXls.properties")
public class WorkPermitToXls {
    private HashMap<Role, AbstractWorker> workersMap;
    private List<AbstractWorker> drivers;
    private List<AbstractWorker> members;
    @Value("#{${work_permit.frontSideHeadKeys}}")
    private List<String> frontSideHeadKeys;
    @Value("#{${work_permit.instructionsKeys}}")
    private List<String> instructionsKeys;
    private List<Cell> instructionsCells = new ArrayList<>();
    @Value("#{${work_permit.reversSideKeys}}")
    private List<String> reversSideKeys;
    @Value("${work_permit.measuresRangeStart}")
    private int measuresRangeStart;
    @Value("${work_permit.measuresRangeEnd}")
    private int measuresRangeEnd;
    @Value("${work_permit.measuresFirstColumnKey}")
    private String measuresFirstColumnKey;
    @Value("${work_permit.measuresSecondColumnKey}")
    private String measuresSecondColumnKey;
    @Value("${work_permit.measuresThirdColumnKey}")
    private String measuresThirdColumnKey;
    @Value("${work_permit.widthMemberStrings}")
    private int[] widthMembersStrings;

    private final Map<String, Cell> frontSideMap = new HashMap<>();
    private final List<Cell[]> stringsMeasures = new ArrayList<>();
    private final Map<String, List<Cell>> reversSideMap = new HashMap<>();
    public String createXlsFile(WorkPermit workPermit, String fileName){
        initWorkersMap(workPermit);

        try (FileInputStream inputStream = new FileInputStream(fileName);
             Workbook workbook = WorkbookFactory.create(inputStream);
             FileOutputStream outputStream = new FileOutputStream(fileName)) {

            Sheet sheet = workbook.getSheet("sheet1");

            initCellsMaps(sheet);

            fillReverseSide(workPermit);
            fillFrontSideHead(workPermit);
            fillInstructions(workPermit);
            fillMeasures(workPermit);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void initWorkersMap(WorkPermit workPermit) {
        workersMap = new HashMap<>();
        drivers = new ArrayList<>();
        members = new ArrayList<>();

        for (RoleAssigment ra : workPermit.getRoleAssigments()) {
            RoleAssigmentPK raPK = ra.getRoleAssigmentPK();
            Role role = raPK.getRole();
            if (role.equals(Role.DRIVER)) {
                drivers.add(raPK.getWorker());
            } else if (role.equals(Role.MEMBER)) {
                members.add(raPK.getWorker());
            } else {
                workersMap.put(role, raPK.getWorker());
            }
        }
    }

    private void fillInstructions(WorkPermit workPermit) {
        List<Instruction> instructions = workPermit.getInstructions();
        int i = 0;
        while(i < instructions.size()){
            instructionsCells.get(i).setCellValue(instructions.get(i).getString());
            i++;
        }
        while(i < instructionsCells.size()){
            instructionsCells.get(i).setCellValue("");
            i++;
        }
    }

    private void fillMeasures(WorkPermit workPermit) {
        List<Measure> measures = workPermit.getMeasures();
        for(int i = 0; i < measures.size() && i < stringsMeasures.size(); i++){
            Cell[] stringMeasure = stringsMeasures.get(i);
            stringMeasure[0].setCellValue(measures.get(i).getFirstColumn());
            stringMeasure[1].setCellValue(measures.get(i).getSecondColumn());
            stringMeasure[2].setCellValue(measures.get(i).getThirdColumn());
        }
        fillEmptyMeasuresCells();
    }

    private void fillEmptyMeasuresCells() {
        for(Cell[] stringMeasure : stringsMeasures){
            for(Cell c : stringMeasure){
                String value = c.getStringCellValue();
                if(value.equals(measuresFirstColumnKey) || value.equals(measuresSecondColumnKey) || value.equals(measuresThirdColumnKey)){
                    c.setCellValue("");
                }
            }
        }
    }

    private void fillFrontSideHead(WorkPermit workPermit) {
        frontSideMap.get("number").setCellValue(workPermit.getNumber());
        for(Map.Entry<Role, AbstractWorker> entry : workersMap.entrySet()){
            if(entry.getKey().equals(Role.ISSUING)){
                insertIssuingWorker(entry.getValue());
            } else {
                fillFrontSideCellForWorker(entry.getValue(), frontSideMap.get(entry.getKey().toString().toLowerCase() + ".header"));
            }
        }

        fillDates(workPermit);
        fillMembers(workPermit);
        fillTask(workPermit.getTask());
        fillTaking();
    }

    private void fillTaking() {
        AbstractWorker taking;
        if(!workersMap.get(Role.ISSUING).equals(workersMap.get(Role.RESPONSIBLE)) && workersMap.get(Role.RESPONSIBLE) instanceof Worker){
            taking = workersMap.get(Role.RESPONSIBLE);
        } else if(!workersMap.get(Role.ISSUING).equals(workersMap.get(Role.PRODUCER)) && workersMap.get(Role.PRODUCER) instanceof Worker){
            taking = workersMap.get(Role.PRODUCER);
        } else if(!workersMap.get(Role.ISSUING).equals(workersMap.get(Role.OBSERVER)) && workersMap.get(Role.OBSERVER) instanceof Worker){
            taking = workersMap.get(Role.OBSERVER);
        } else {
            taking = new AbstractWorker();
            taking.setName("");
        }
        frontSideMap.get("taking").setCellValue(taking.getName());
    }

    private void fillMembers(WorkPermit workPermit) {
        int stringCount = 0;
        StringBuilder[] strings = new StringBuilder[3];
        for(int i = 0; i < strings.length; i++){
            strings[i] = new StringBuilder();
        }

        String[] technics = workPermit.getTechnicList()
                .stream()
                .map(Technic::getName)
                .map(x -> x.split(" "))
                .flatMap(Arrays::stream)
                .toArray(String[]::new);


        for (int i = 0; i < members.size() && stringCount < 3; i++) {
            AbstractWorker w = members.get(i);
            String insertString = w.getName() + " " + w.getAccessGroup() + "гр, ";
            if (strings[stringCount].length() + insertString.length() < widthMembersStrings[stringCount]) {
                strings[stringCount].append(insertString);
            } else {
                stringCount++;
                i--;
            }
        }

        for(int i = 0; i < drivers.size() && stringCount < 3; i++){
            AbstractWorker w = drivers.get(i);
            String insertString = w.getName() + " " + w.getAccessGroup() + "гр-водитель, ";
            if (strings[stringCount].length() + insertString.length() < widthMembersStrings[stringCount]) {
                strings[stringCount].append(insertString);
            } else {
                stringCount++;
                i--;
            }
        }

        for(int i = 0; i < technics.length && stringCount < 3; i++){
            String oneWord = technics[i];
            if(strings[stringCount].length() + oneWord.length() < widthMembersStrings[stringCount]){
                strings[stringCount].append(oneWord).append(" ");
            } else {
                stringCount++;
                i--;
            }
        }

        for(int i = 0; i < strings.length; i++){
            frontSideMap.get("members" + i).setCellValue(strings[i].toString());
        }
    }

    private void fillTask(List<Task> tasks) {
        int i = 0;
        for( ; i < tasks.size(); i++){
            frontSideMap.get("task" + i).setCellValue(tasks.get(i).getString());
        }
        for( ; i < 3; i++){
            frontSideMap.get("task" + i).setCellValue("");
        }
    }

    private void insertIssuingWorker(AbstractWorker worker) {
        frontSideMap.get("issuingAndGroup").setCellValue(worker.getName() + " " + worker.getAccessGroup() + "гр");
        frontSideMap.get("issuing").setCellValue(worker.getName());
    }

    private void fillFrontSideCellForWorker(AbstractWorker worker, Cell cell){
        if(worker instanceof Worker){
            cell.setCellValue(worker.getDativeName() + " " + worker.getAccessGroup() + "гр.");
        } else {
            cell.setCellValue(worker.getName());
        }
    }

    private void fillDates(WorkPermit workPermit) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDateTime startDateTime = workPermit.getStartDateTime();
        frontSideMap.get("startDate").setCellValue(startDateTime.format(dateFormatter));
        frontSideMap.get("startTime").setCellValue(startDateTime.format(timeFormatter));

        LocalDateTime endDateTime = workPermit.getEndDateTime();
        frontSideMap.get("endDate").setCellValue(endDateTime.format(dateFormatter));
        frontSideMap.get("endTime").setCellValue(endDateTime.format(timeFormatter));

        LocalDateTime issuingDateTime = workPermit.getIssuingDateTime();
        frontSideMap.get("issuingDate").setCellValue(issuingDateTime.format(dateFormatter));
        frontSideMap.get("issuingTime").setCellValue(issuingDateTime.format(timeFormatter));
    }

    private void fillReverseSide(WorkPermit workPermit) {
        int memberCount = 1;
        for(Map.Entry<Role, AbstractWorker> entry : workersMap.entrySet()) {
            if(entry.getKey() == Role.ISSUING || entry.getKey() == Role.OBSERVER){
                continue;
            } else {
                fillReverseSideCellsForWorker(entry.getValue(), reversSideMap.get(entry.getKey().toString().toLowerCase()));
            }
        }

        for(AbstractWorker worker : members){
            fillReverseSideCellsForWorker(worker, reversSideMap.get("member" + memberCount));
            memberCount++;
        }

        for(AbstractWorker worker : drivers){
            fillReverseSideCellsForWorker(worker, reversSideMap.get("member" + memberCount));
            memberCount++;
        }

        if(memberCount < 6){
            Worker worker = new Worker();
            worker.setName("");
            for( ; memberCount < 6; memberCount++){
                fillReverseSideCellsForWorker(worker, reversSideMap.get("member" + memberCount));
            }
        }
    }

    private void fillReverseSideCellsForWorker(AbstractWorker worker, List<Cell> cells){
        if(worker instanceof Worker){
            for(Cell cell : cells){
                cell.setCellValue(worker.getName());
            }
        } else {
            for(Cell cell : cells){
                cell.setCellValue("");
            }
        }
    }

    private void initCellsMaps(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            if (row.getRowNum() >= measuresRangeStart && row.getRowNum() <= measuresRangeEnd) {
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell[] cells = new Cell[3];
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    if(value.isEmpty()){
                        continue;
                    }
                    if(value.equals(measuresFirstColumnKey)){
                        cells[0] = cell;
                    } else if(value.equals(measuresSecondColumnKey)){
                        cells[1] = cell;
                    } else if(value.equals(measuresThirdColumnKey)){
                        cells[2] = cell;
                    }
                }
                stringsMeasures.add(cells);
            } else {

                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    String value = cell.getStringCellValue();
                    if (value.isEmpty()) {
                        continue;
                    }

                    if (reversSideKeys.contains(value)) {
                        if (!reversSideMap.containsKey(value)) {
                            reversSideMap.put(value, new ArrayList<>());
                        }
                        reversSideMap.get(value).add(cell);
                    }

                    if (frontSideHeadKeys.contains(value)) {
                        frontSideMap.put(value, cell);
                    }

                    if(instructionsKeys.contains(value)){
                        instructionsCells.add(cell);
                    }
                }
            }
        }
    }

}
