package tihonel.com.github.workpermit.dto;

import tihonel.com.github.workpermit.models.workpermit.Measure;
import tihonel.com.github.workpermit.models.Role;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DtoWorkPermit {
    private int id;
    private HashMap<Integer, List<Role>> workerIdRolesMap;
    private String shortName;

    private String number;
    private String task;
    private String startDateTime;
    private String endDateTime;
    private String issuingDateTime;
    private String instructions;
    private List<Measure> measures;

    private int responsible;
    private int admitting;
    private int producer;
    private int observer;
    private int issuing;
    private List<Integer> brigadeMembers;
    private List<Integer> drivers;
    private List<Integer> technicsList;

    private int receiving;

    private void initWorkerIdRolesMap(){
        workerIdRolesMap = new HashMap<>();
        if (this.getBrigadeMembers() != null) {
            this.getBrigadeMembers().forEach(x -> workerIdRolesMap.put(x, new ArrayList<>(List.of(Role.MEMBER))));
        }

        if (this.getDrivers() != null) {
            this.getDrivers().forEach(x -> workerIdRolesMap.put(x, new ArrayList<>(List.of(Role.DRIVER))));
        }

        Class<?> clazz = this.getClass();

        // Обходим все поля класса
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals("receiving") && field.getType() == int.class && !field.getName().equals("id")) {
                field.setAccessible(true);
                try {
                    int id = field.getInt(this);
                    if(!workerIdRolesMap.containsKey(id)){
                        workerIdRolesMap.put(id, new ArrayList<>());
                    }
                    System.out.println(field.getName().toUpperCase());

                    workerIdRolesMap.get(id).add(Role.valueOf(field.getName().toUpperCase()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public List<Integer> getBrigadeMembers() {
        return brigadeMembers;
    }

    public void setBrigadeMembers(List<Integer> brigadeMembers) {
        this.brigadeMembers = brigadeMembers;
    }

    public int getResponsible() {
        return responsible;
    }

    public void setResponsible(int responsible) {
        this.responsible = responsible;
    }

    public int getAdmitting() {
        return admitting;
    }

    public void setAdmitting(int admitting) {
        this.admitting = admitting;
    }

    public int getProducer() {
        return producer;
    }

    public void setProducer(int producer) {
        this.producer = producer;
    }

    public int getObserver() {
        return observer;
    }

    public void setObserver(int observer) {
        this.observer = observer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public List<Integer> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Integer> drivers) {
        this.drivers = drivers;
    }

    public int getIssuing() {
        return issuing;
    }

    public void setIssuing(int issuing) {
        this.issuing = issuing;
    }

    public List<Integer> getTechnicsList() {
        return technicsList;
    }

    public void setTechnicsList(List<Integer> technicsList) {
        this.technicsList = technicsList;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getIssuingDateTime() {
        return issuingDateTime;
    }

    public void setIssuingDateTime(String issuingDateTime) {
        this.issuingDateTime = issuingDateTime;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public HashMap<Integer, List<Role>> getWorkerIdRolesMap() {
        if(workerIdRolesMap == null){
            initWorkerIdRolesMap();
        }
        return workerIdRolesMap;
    }

    public void setWorkerIdRolesMap(HashMap<Integer, List<Role>> workerIdRolesMap) {
        this.workerIdRolesMap = workerIdRolesMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiving() {
        return receiving;
    }

    public void setReceiving(int receiving) {
        this.receiving = receiving;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
