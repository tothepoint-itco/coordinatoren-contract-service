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

    @NotNull(message = "contract.error.bediendeid.notnull")
    private String bediendeId;

    @NotNull(message = "contract.error.businessunitid.notnull")
    private String businessUnitId;

    @NotNull(message = "contract.error.startdatum.notnull")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;

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

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate getStartDatum() {
        return startDatum;
    }

    @JsonFormat(pattern = "d/M/yyyy")
    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate getEindDatum() {
        return eindDatum;
    }

    @JsonFormat(pattern = "d/M/yyyy")
    public void setEindDatum(LocalDate eindDatum) {
        this.eindDatum = eindDatum;
    }
}
