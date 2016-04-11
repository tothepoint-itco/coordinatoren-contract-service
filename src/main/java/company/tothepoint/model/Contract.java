package company.tothepoint.model;


import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Contract {
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 255)
    private String naam;

    public Contract() {
    }

    public Contract(String naam) {
        this.naam = naam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    static class Notification {
        private String title;
        private LocalDateTime dateTimeStamp;

        Notification() {
        }

        Notification(String title) {
            this.title = title;
            this.dateTimeStamp = LocalDateTime.now();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LocalDateTime getDateTimeStamp() {
            return dateTimeStamp;
        }

        public void setDateTimeStamp(LocalDateTime dateTimeStamp) {
            this.dateTimeStamp = dateTimeStamp;
        }
    }
}
