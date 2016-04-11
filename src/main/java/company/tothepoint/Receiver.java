package company.tothepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.model.BusinessUnit;
import company.tothepoint.model.NewBusinessUnitNotification;
import company.tothepoint.model.Notification;
import company.tothepoint.repository.BusinessUnitRepository;
import company.tothepoint.repository.ContractRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

class Receiver implements MessageListener {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message) {
        Optional<Notification> notification;
        try {
            notification = Optional.of(objectMapper.readValue(message.getBody(), Notification.class));
        } catch(IOException e ) {
            notification = Optional.empty();
        }

        notification.ifPresent( not -> {

            switch(not.getTitle()) {
                case "businessUnitCreated":
                    System.out.println("RECEIVED EVENT: A new business unit was created!");
                    reactToMessage(businessUnitRepository::save, message.getBody());
                    break;

                case "businessUnitUpdated":
                    System.out.println("RECEIVED EVENT: A new business unit was updated!");
                    reactToMessage(businessUnitRepository::save, message.getBody());
                    break;

                case "businessUnitDeleted":
                    System.out.println("RECEIVED EVENT: A new business unit was deleted!");
                    break;
                default:
                    System.out.println("Unknown event received");
            }

        });

    }

    private void reactToMessage(Function<BusinessUnit, BusinessUnit> repoFunction, byte[] message){
        Optional<NewBusinessUnitNotification> buNotification;
        System.out.println("debug1");

        try {
            buNotification = Optional.of(objectMapper.readValue(message, NewBusinessUnitNotification.class));
        } catch (IOException e) {
            buNotification = Optional.empty();
        }

        buNotification.ifPresent( buNot -> {
            System.out.println("Saving business unit to the repo");
            BusinessUnit buToSave = buNot.getBusinessUnit();
            buToSave.setNaam("Hallo wereld :)");
            repoFunction.apply(buNot.getBusinessUnit());
            System.out.println("Saved to repo :)");
        });
    }


}
