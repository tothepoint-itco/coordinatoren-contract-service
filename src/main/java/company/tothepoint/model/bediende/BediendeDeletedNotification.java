package company.tothepoint.model.bediende;

import company.tothepoint.model.Notification;

public class BediendeDeletedNotification extends Notification {
    private String deletedBediendeId;

    public BediendeDeletedNotification() {
    }

    public BediendeDeletedNotification(String title, String deletedBediendeId) {
        super(title);
        this.deletedBediendeId = deletedBediendeId;
    }

    public String getDeletedBediendeId() {
        return deletedBediendeId;
    }

    public void setDeletedBediendeId(String deletedBediendeId) {
        this.deletedBediendeId = deletedBediendeId;
    }
}
