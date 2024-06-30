package tihonel.com.github.workpermit.dto;

import tihonel.com.github.workpermit.models.ReportEntry;
import tihonel.com.github.workpermit.models.workpermit.Task;

import java.util.List;

public class SimpleDtoWorkPermit {
    private int id;
    private List<Task> taskList;
    private String number;
    private List<String> drivers;
    private List<String> members;
    private String responsible;
    private String admitting;
    private String producer;
    private String observer;
    private String issuing;

    private List<ReportEntry> reportEntries;


    public SimpleDtoWorkPermit() {
    }

    public SimpleDtoWorkPermit(int id, List<Task> taskList, String number) {
        this.id = id;
        this.taskList = taskList;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<String> drivers) {
        this.drivers = drivers;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getAdmitting() {
        return admitting;
    }

    public void setAdmitting(String admitting) {
        this.admitting = admitting;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getObserver() {
        return observer;
    }

    public void setObserver(String observer) {
        this.observer = observer;
    }

    public String getIssuing() {
        return issuing;
    }

    public void setIssuing(String issuing) {
        this.issuing = issuing;
    }

    public List<ReportEntry> getReportEntries() {
        return reportEntries;
    }

    public void setReportEntries(List<ReportEntry> reportEntries) {
        this.reportEntries = reportEntries;
    }
}
