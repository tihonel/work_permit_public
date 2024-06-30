package tihonel.com.github.workpermit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tihonel.com.github.workpermit.dto.DtoReport;
import tihonel.com.github.workpermit.models.HistoricalPrice;
import tihonel.com.github.workpermit.models.ReportEntry;
import tihonel.com.github.workpermit.models.workpermit.WorkPermit;
import tihonel.com.github.workpermit.repositories.HistoricalPricesRepository;
import tihonel.com.github.workpermit.repositories.ReportEntryRepository;
import tihonel.com.github.workpermit.repositories.WorkPermitsRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ReportEntriesService {
    private final WorkPermitsRepository workPermitsRepository;
    private final HistoricalPricesRepository historicalPricesRepository;
    private final ReportEntryRepository reportEntryRepository;
    private final DateTimeFormatter dateFormatter;

    @Autowired
    public ReportEntriesService(WorkPermitsRepository workPermitsRepository, HistoricalPricesRepository historicalPricesRepository, ReportEntryRepository reportEntryRepository, DateTimeFormatter dateFormatter) {
        this.workPermitsRepository = workPermitsRepository;
        this.historicalPricesRepository = historicalPricesRepository;
        this.reportEntryRepository = reportEntryRepository;
        this.dateFormatter = dateFormatter;
    }


    public void saveNewReportEntry(DtoReport dtoReport, int workPermitId){
        LocalDate dateOfWork = LocalDate.parse(dtoReport.getDate(), dateFormatter);
        HistoricalPrice historicalPrice = historicalPricesRepository.findHistoricalPriceByPriceIdAndDayOfWork(dtoReport.getPriceId(), dateOfWork);
        WorkPermit workPermit = workPermitsRepository.findById(workPermitId).get();
        ReportEntry reportEntry = new ReportEntry(dateOfWork, dtoReport.getQuantity(), workPermit, historicalPrice);
        reportEntryRepository.save(reportEntry);
    }

    public Optional<Integer> deleteById(int id) {
        Optional<Integer> workPermitId = Optional.empty();
        Optional<ReportEntry> reportEntryOptional = reportEntryRepository.findById(id);
        if(reportEntryOptional.isPresent()){
            ReportEntry reportEntry = reportEntryOptional.get();
            workPermitId = Optional.of(reportEntry.getWorkPermit().getId());
            reportEntryRepository.delete(reportEntry);
        }
        return workPermitId;
    }
}
