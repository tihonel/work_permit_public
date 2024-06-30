package tihonel.com.github.workpermit.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tihonel.com.github.workpermit.dto.DtoReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DtoReportValidator implements Validator {
    private LocalDate beginningDate = LocalDate.of(2024, 1, 1);
    private DateTimeFormatter dateFormatter;

    @Autowired
    public DtoReportValidator(DateTimeFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DtoReport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DtoReport dtoReport = (DtoReport) target;
        try {
            LocalDate dateOfWork = LocalDate.parse(dtoReport.getDate(), dateFormatter);
            if(dateOfWork.isBefore(beginningDate)){
                errors.rejectValue("date", "", "Дата должна быть после 01.01.2024");
            }
        } catch (DateTimeParseException e){
            errors.rejectValue("date", "", "Попробуйте ввести дату в формате гггг-мм-дд");
        }
    }
}
