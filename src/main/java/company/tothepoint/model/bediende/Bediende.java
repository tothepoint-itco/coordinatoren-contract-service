package company.tothepoint.model.bediende;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Bediende {
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 32)
    private String voorNaam;

    @NotNull
    @Size(min = 1, max = 127)
    private String familieNaam;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate geboorteDatum;
    @NotNull
    private String telefoonNummer;

    public Bediende() {
    }

    public Bediende(String voorNaam, String familieNaam, LocalDate geboorteDatum, String telefoonNummer) {
        this.voorNaam = voorNaam;
        this.familieNaam = familieNaam;
        this.geboorteDatum = geboorteDatum;
        this.telefoonNummer = telefoonNummer;
    }

    public String getTelefoonNummer() {
        return telefoonNummer;
    }

    public void setTelefoonNummer(String telefoonNummer) {
        this.telefoonNummer = telefoonNummer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoorNaam() {
        return voorNaam;
    }

    public void setVoorNaam(String voorNaam) {
        this.voorNaam = voorNaam;
    }

    public String getFamilieNaam() {
        return familieNaam;
    }

    public void setFamilieNaam(String familieNaam) {
        this.familieNaam = familieNaam;
    }

    public LocalDate getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(LocalDate geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }
}
