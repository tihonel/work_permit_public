package tihonel.com.github.workpermit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tihonel.com.github.workpermit.dao.DaoInstruction;
import tihonel.com.github.workpermit.dao.DaoTask;
import tihonel.com.github.workpermit.dto.DtoWorkPermit;
import tihonel.com.github.workpermit.dto.SimpleDtoWorkPermit;
import tihonel.com.github.workpermit.models.*;
import tihonel.com.github.workpermit.models.worker.AbstractWorker;
import tihonel.com.github.workpermit.models.workpermit.Instruction;
import tihonel.com.github.workpermit.models.workpermit.Measure;
import tihonel.com.github.workpermit.models.workpermit.Task;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;
import tihonel.com.github.workpermit.services.TechnicsService;
import tihonel.com.github.workpermit.services.WorkersService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DtoWorkPermitMapper {
    private final DateTimeFormatter dateTimeFormatter;
    private final WorkersService workersService;
    private final TechnicsService technicsService;
    private final DaoInstruction daoInstruction;
    private final DaoTask daoTask;

    @Autowired
    public DtoWorkPermitMapper(DateTimeFormatter dateTimeFormatter, WorkersService workersService, TechnicsService technicsService, DaoInstruction daoInstruction, DaoTask daoTask) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.workersService = workersService;
        this.technicsService = technicsService;
        this.daoInstruction = daoInstruction;
        this.daoTask = daoTask;
    }

    public SimpleDtoWorkPermit getSimpleDtoWorkPermit(WorkPermit workPermit){
        SimpleDtoWorkPermit simpleDtoWorkPermit = new SimpleDtoWorkPermit(workPermit.getId(), workPermit.getTask(), workPermit.getNumber());

        Class<?> clazz = simpleDtoWorkPermit.getClass();
        Map<String, Field> fieldMap = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, x -> x ));

        List<RoleAssigment> roleAssigments = workPermit.getRoleAssigments();
        List<String> drivers = new ArrayList<>();
        List<String> members = new ArrayList<>();
        for(RoleAssigment ra : roleAssigments){
            RoleAssigmentPK raPK = ra.getRoleAssigmentPK();
            Role role = raPK.getRole();
            AbstractWorker worker = raPK.getWorker();
            if(role == Role.MEMBER){
                members.add(worker.getName());
            } else if(role == Role.DRIVER){
                drivers.add(worker.getName());
            } else {
                Field field = fieldMap.get(role.toString().toLowerCase());
                field.setAccessible(true);
                try {
                    field.set(simpleDtoWorkPermit, worker.getName());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        simpleDtoWorkPermit.setMembers(members);
        simpleDtoWorkPermit.setDrivers(drivers);
        simpleDtoWorkPermit.setReportEntries(workPermit.getReportEntries());
        simpleDtoWorkPermit.getReportEntries().sort(Comparator.comparing(ReportEntry::getDate));

        return simpleDtoWorkPermit;
    }

    public WorkPermit createWorkPermitFromDto(DtoWorkPermit dtoWorkPermit){
        WorkPermit workPermit = new WorkPermit();

        workPermit.setId(dtoWorkPermit.getId());
        workPermit.setNumber(dtoWorkPermit.getNumber());

        setTaskFromDto(dtoWorkPermit, workPermit);

        workPermit.setShortName(dtoWorkPermit.getShortName());

        workPermit.setStartDateTime(LocalDateTime.parse(dtoWorkPermit.getStartDateTime(), dateTimeFormatter));
        workPermit.setEndDateTime(LocalDateTime.parse(dtoWorkPermit.getEndDateTime(), dateTimeFormatter));
        workPermit.setIssuingDateTime(LocalDateTime.parse(dtoWorkPermit.getIssuingDateTime(), dateTimeFormatter));

        setMeasuresFromDto(dtoWorkPermit, workPermit);

        setInstructionsFromDto(dtoWorkPermit, workPermit);

        setRoleAssigmentsFromDto(dtoWorkPermit, workPermit);
        technicsService.findAllById(dtoWorkPermit.getTechnicsList()).forEach(System.out::println);
        workPermit.setTechnicList(technicsService.findAllById(dtoWorkPermit.getTechnicsList()));

        return workPermit;
    }

    private void setMeasuresFromDto(DtoWorkPermit dtoWorkPermit, WorkPermit workPermit){
        deleteEmptyMeasures(dtoWorkPermit.getMeasures());
        dtoWorkPermit.getMeasures().forEach(x -> x.setWorkPermit(workPermit));
        workPermit.setMeasures(dtoWorkPermit.getMeasures());
    }

    private void setInstructionsFromDto(DtoWorkPermit dtoWorkPermit, WorkPermit workPermit) {
        String[] strings = dtoWorkPermit.getInstructions().split("\n");
        ArrayList<Instruction> instructions = new ArrayList<>();
        for(String s : strings){
            if (!s.isEmpty()) {
                instructions.add(new Instruction(s, workPermit));
            }
        }

        List<Integer> idsInstruction = daoInstruction.getIdsInstructionByWorkPermitId(workPermit.getId());
        for(int i = 0; i < idsInstruction.size(); i++){
            instructions.get(i).setId(idsInstruction.get(i));
        }
        workPermit.setInstructions(instructions);
    }

    private void setTaskFromDto(DtoWorkPermit dtoWorkPermit, WorkPermit workPermit) {
        String[] strings = dtoWorkPermit.getTask().split("\n");
        ArrayList<Task> tasks = new ArrayList<>();
        for(String s : strings){
            if (!s.isEmpty()) {
                tasks.add(new Task(s, workPermit));
            }
        }

        List<Integer> idsTask = daoTask.getIdsTaskByWorkPermitId(workPermit.getId());
        for(int i = 0; i < idsTask.size(); i++){
            tasks.get(i).setId(idsTask.get(i));
        }

        workPermit.setTask(tasks);
    }

    private void setRoleAssigmentsFromDto(DtoWorkPermit dtoWorkPermit, WorkPermit workPermit) {
        List<RoleAssigment> roleAssigmentList = new ArrayList<>();
        for(Map.Entry<Integer, List<Role>> e : dtoWorkPermit.getWorkerIdRolesMap().entrySet()) {
            AbstractWorker worker = workersService.findAbstractWorkerById(e.getKey());
            for (Role role : e.getValue()) {
                RoleAssigmentPK raPK = new RoleAssigmentPK(workPermit, worker, role);
                RoleAssigment ra = new RoleAssigment(raPK);
                roleAssigmentList.add(ra);
            }
        }
        workPermit.setRoleAssigments(roleAssigmentList);
    }

    private void deleteEmptyMeasures(List<Measure> measures){
        int size = measures.size() - 1;
        for(int i = size; i >= 0; i--){
            if(measures.get(i).isEmpty()){
                measures.remove(i);
            }
        }
    }

    public DtoWorkPermit getDtoWorkPermit(WorkPermit workPermit) {
        DtoWorkPermit dtoWorkPermit = new DtoWorkPermit();
        dtoWorkPermit.setId(workPermit.getId());
        dtoWorkPermit.setNumber(workPermit.getNumber());
        setTaskFromWorkPermit(workPermit, dtoWorkPermit);
        dtoWorkPermit.setShortName(workPermit.getShortName());
        setDatesFromWorkPermit(workPermit, dtoWorkPermit);
        setInstructionsFromWorkPermit(workPermit, dtoWorkPermit);
        setMeasuresFromWorkPermit(workPermit, dtoWorkPermit);
        setWorkersFromWorkPermit(workPermit, dtoWorkPermit);
        setTechnicsListFromWorkPermit(workPermit, dtoWorkPermit);
        return dtoWorkPermit;
    }

    private void setTechnicsListFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        dtoWorkPermit.setTechnicsList(workPermit.getTechnicList().stream()
                                                                    .map(Technic::getId)
                                                                    .toList());
    }

    private void setWorkersFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        Class<?> clazz = dtoWorkPermit.getClass();
        Map<String, Field> fieldMap = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, x -> x ));

        List<RoleAssigment> roleAssigments = workPermit.getRoleAssigments();
        List<Integer> drivers = new ArrayList<>();
        List<Integer> members = new ArrayList<>();
        for(RoleAssigment ra : roleAssigments){
            RoleAssigmentPK raPK = ra.getRoleAssigmentPK();
            Role role = raPK.getRole();
            AbstractWorker worker = raPK.getWorker();
            if(role == Role.MEMBER){
                members.add(worker.getId());
            } else if(role == Role.DRIVER){
                drivers.add(worker.getId());
            } else {
                Field field = fieldMap.get(role.toString().toLowerCase());
                field.setAccessible(true);
                try {
                    field.set(dtoWorkPermit, worker.getId());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        dtoWorkPermit.setBrigadeMembers(members);
        dtoWorkPermit.setDrivers(drivers);
    }

    private void setMeasuresFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        System.out.println("Метод маппера");
        workPermit.getMeasures().forEach(System.out::println);
        dtoWorkPermit.setMeasures(workPermit.getMeasures());
        List<Measure> measuresDto = dtoWorkPermit.getMeasures();
        measuresDto.sort(Comparator.comparingInt(Measure::getId));
        while (measuresDto.size() < 13){
            measuresDto.add(new Measure());
        }

    }

    private void setInstructionsFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        dtoWorkPermit.setInstructions(workPermit.getInstructions().stream()
                .sorted(Comparator.comparingInt(Instruction::getId))
                .map(Instruction::getString)
                .collect(Collectors.joining("\n")));
    }

    private void setDatesFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        dtoWorkPermit.setStartDateTime(workPermit.getStartDateTime().format(dateTimeFormatter));
        dtoWorkPermit.setEndDateTime(workPermit.getEndDateTime().format(dateTimeFormatter));
        dtoWorkPermit.setIssuingDateTime(workPermit.getIssuingDateTime().format(dateTimeFormatter));
    }

    private void setTaskFromWorkPermit(WorkPermit workPermit, DtoWorkPermit dtoWorkPermit) {
        dtoWorkPermit.setTask(workPermit.getTask().stream()
                .sorted(Comparator.comparingInt(Task::getId))
                .map(Task::getString)
                .collect(Collectors.joining("\n")));
    }
}
