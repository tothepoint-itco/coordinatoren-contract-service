package company.tothepoint.model.contract;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Contract {
    @Id
    private String id;

    @NotNull
    private String bediendeId;

    @NotNull
    private String businessUnitId;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate eindDatum;


    public Contract() {
    }

    public Contract(String bediendeId, String businessUnitId, LocalDate startDatum) {
        this.bediendeId = bediendeId;
        this.businessUnitId = businessUnitId;
        this.startDatum = startDatum;
    }

    public Contract(String bediendeId, String businessUnitId, LocalDate startDatum, LocalDate eindDatum) {
        this.bediendeId = bediendeId;
        this.businessUnitId = businessUnitId;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBediendeId() {
        return bediendeId;
    }

    public void setBediendeId(String bediendeId) {
        this.bediendeId = bediendeId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public LocalDate getEindDatum() {
        return eindDatum;
    }

    public void setEindDatum(LocalDate eindDatum) {
        this.eindDatum = eindDatum;
    }
}
