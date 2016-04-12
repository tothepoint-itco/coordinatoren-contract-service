package company.tothepoint.model.bediende;

import company.tothepoint.model.Notification;

public class BediendeCreatedNotification extends Notification {
    private Bediende createdBediende;

    public BediendeCreatedNotification() {
    }

    public BediendeCreatedNotification(String title, Bediende createdBediende) {
        super(title);
        this.createdBediende = createdBediende;
    }

    public Bediende getCreatedBediende() {
        return createdBediende;
    }

    public void setCreatedBediende(Bediende createdBediende) {
        this.createdBediende = createdBediende;
    }
}
