package tihonel.com.github.workpermit.dto;

import java.time.LocalDate;

public class DtoReportForTime {
    public String startDate;
    public String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
