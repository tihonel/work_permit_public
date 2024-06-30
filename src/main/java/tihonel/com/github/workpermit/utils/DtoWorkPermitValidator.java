package tihonel.com.github.workpermit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import tihonel.com.github.workpermit.dto.DtoWorkPermit;
import tihonel.com.github.workpermit.models.workpermit.Measure;
import tihonel.com.github.workpermit.models.Role;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class DtoWorkPermitValidator implements Validator {
    private Map<Integer, List<Role>> workerRolesMap;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public DtoWorkPermitValidator(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return DtoWorkPermit.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DtoWorkPermit dtoWorkPermit = (DtoWorkPermit) target;
        workerRolesMap = dtoWorkPermit.getWorkerIdRolesMap();
        validateNumber(dtoWorkPermit, errors);
        validateQuantityMeasures(dtoWorkPermit, errors);
        validateQuantityMembers(dtoWorkPermit, (BindingResult) errors);
        validateQuantityRolesForOneWorker((BindingResult) errors);
        validateAllDateTime(dtoWorkPermit, errors);
        validateShortName(dtoWorkPermit, errors);
    }

    private void validateShortName(DtoWorkPermit dtoWorkPermit, Errors errors) {
        if(dtoWorkPermit.getShortName() == null || dtoWorkPermit.getShortName().trim().isEmpty()){
            errors.rejectValue("shortName", "", "Поле обязательно к заполнению");
        }
    }

    private void validateNumber(DtoWorkPermit dtoWorkPermit, Errors errors) {
        if(dtoWorkPermit.getNumber() == null || dtoWorkPermit.getNumber().isEmpty()){
            errors.rejectValue("number", "", "Номер наряда должен быть заполнен");
        }
    }

    private void validateQuantityMeasures(DtoWorkPermit dtoWorkPermit, Errors errors) {
        if(dtoWorkPermit.getMeasures() == null || dtoWorkPermit.getMeasures().isEmpty()){
            errors.rejectValue("measures", "", "Мероприятия не должны быть пустыми");
        } else {
            boolean tableNotContainsMeasures = true;
            for(Measure m : dtoWorkPermit.getMeasures()){
                if(!m.isEmpty()){
                    tableNotContainsMeasures = false;
                }
            }
            if(tableNotContainsMeasures){
                errors.rejectValue("measures", "", "Мероприятия не должны быть пустыми");
            }
        }
    }

    private void validateQuantityMembers(DtoWorkPermit dtoWorkPermit, BindingResult errors) {
        List<Integer> brigadeMembers = dtoWorkPermit.getBrigadeMembers();
        List<Integer> drivers = dtoWorkPermit.getDrivers();

        if((brigadeMembers == null || brigadeMembers.isEmpty()) &&
                (drivers == null || drivers.isEmpty())){
            ObjectError glo = new ObjectError("globalError", "Должен быть хотя бы один член бригады");
            errors.addError(glo);
        }

        int brigadeMembersSize = brigadeMembers == null ? 0 : brigadeMembers.size();
        int driversSize = drivers == null ? 0 : drivers.size();

        if(brigadeMembersSize + driversSize > 5){
            ObjectError glo = new ObjectError("globalError", "Максимум членов бригады 5");
            errors.addError(glo);
        }
    }

    private void validateAllDateTime(DtoWorkPermit dtoWorkPermit, Errors errors){
        Class<?> clazz = dtoWorkPermit.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if(field.toString().endsWith("DateTime")){
                try {
                    field.setAccessible(true);
                    String stringDateTime = (String) field.get(dtoWorkPermit);
                    if(stringDateTime == null || stringDateTime.isEmpty()){
                        errors.rejectValue(field.getName(), "","Дата не может быть пустой");
                        continue;
                    }
                    LocalDateTime.parse(stringDateTime, dateTimeFormatter);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (DateTimeParseException e){
                    errors.rejectValue(field.getName(), "","Дата должна быть в формате: дд.ММ.гггг ЧЧ:мм");
                }
            }
        }
    }

    private void validateQuantityRolesForOneWorker(BindingResult errors) {
        for(Map.Entry<Integer, List<Role>> e : workerRolesMap.entrySet()){
            if(e.getValue().size() > 2){
                ObjectError glo = new ObjectError("globalError", "Один или несколько работников задействованы более двух раз");
                errors.addError(glo);
                break;
            }
        }
    }
}
