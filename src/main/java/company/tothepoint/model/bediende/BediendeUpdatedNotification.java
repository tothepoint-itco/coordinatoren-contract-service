package company.tothepoint.model.bediende;

import company.tothepoint.model.Notification;

public class BediendeUpdatedNotification extends Notification {
    private Bediende updatedBediende;

    public BediendeUpdatedNotification() {
    }

    public BediendeUpdatedNotification(String title, Bediende updatedBediende) {
        super(title);
        this.updatedBediende = updatedBediende;
    }

    public Bediende getUpdatedBediende() {
        return updatedBediende;
    }

    public void setUpdatedBediende(Bediende updatedBediende) {
        this.updatedBediende = updatedBediende;
    }
}
