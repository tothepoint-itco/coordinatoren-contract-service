package company.tothepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.tothepoint.model.Notification;
import company.tothepoint.model.bediende.BediendeCreatedNotification;
import company.tothepoint.model.bediende.BediendeDeletedNotification;
import company.tothepoint.model.bediende.BediendeUpdatedNotification;
import company.tothepoint.model.businessunit.BusinessUnitCreatedNotification;
import company.tothepoint.model.businessunit.BusinessUnitDeletedNotification;
import company.tothepoint.model.businessunit.BusinessUnitUpdatedNotification;
import company.tothepoint.repository.BediendeRepository;
import company.tothepoint.repository.BusinessUnitRepository;
import company.tothepoint.repository.ContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

class Receiver implements MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BediendeRepository bediendeRepository;

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
                    LOG.debug("EVENT RECEIVED: A business unit was created!");
                    Optional<BusinessUnitCreatedNotification> newBusinessUnit;

                    try {
                        newBusinessUnit = Optional.of(objectMapper.readValue(message.getBody(), BusinessUnitCreatedNotification.class));
                    } catch (IOException e ) {
                        newBusinessUnit = Optional.empty();
                    }

                    newBusinessUnit.ifPresent( businessUnitToCreate ->
                        businessUnitRepository.save(businessUnitToCreate.getCreatedBusinessUnit())
                    );
                    break;

                case "businessUnitUpdated":
                    LOG.debug("EVENT RECEIVED: A business unit was updated!");
                    Optional<BusinessUnitUpdatedNotification> updatedBusinessUnit;

                    try {
                        updatedBusinessUnit = Optional.of(objectMapper.readValue(message.getBody(), BusinessUnitUpdatedNotification.class));
                    } catch (IOException e) {
                        updatedBusinessUnit = Optional.empty();
                    }

                    updatedBusinessUnit.ifPresent( businessUnitToUpdate ->
                        businessUnitRepository.save(businessUnitToUpdate.getUpdatedBusinessUnit())
                    );
                    break;

                case "businessUnitDeleted":
                    LOG.debug("EVENT RECEIVED: A business unit was deleted!");
                    Optional<BusinessUnitDeletedNotification> deletedBusinessUnit;

                    try {
                        deletedBusinessUnit = Optional.of(objectMapper.readValue(message.getBody(), BusinessUnitDeletedNotification.class));
                    } catch (IOException e) {
                        deletedBusinessUnit = Optional.empty();
                    }

                    deletedBusinessUnit.ifPresent( businessUnitToDelete ->
                        businessUnitRepository.delete(businessUnitToDelete.getDeletedBusinessUnitId())
                    );

                    break;


                case "bediendeCreated":
                    LOG.debug("EVENT RECEIVED: A bediende was created!");
                    Optional<BediendeCreatedNotification> newBediende;

                    try {
                        newBediende = Optional.of(objectMapper.readValue(message.getBody(), BediendeCreatedNotification.class));
                    } catch (IOException e) {
                        newBediende = Optional.empty();
                    }

                    newBediende.ifPresent( bediendeToCreate ->
                        bediendeRepository.save(bediendeToCreate.getCreatedBediende())
                    );
                    break;

                case "bediendeUpdated":
                    LOG.debug("EVENT RECEIVED: A bediende was updated!");
                    Optional<BediendeUpdatedNotification> updatedBediende;

                    try {
                        updatedBediende = Optional.of(objectMapper.readValue(message.getBody(), BediendeUpdatedNotification.class));
                    } catch (IOException e) {
                        updatedBediende = Optional.empty();
                    }

                    updatedBediende.ifPresent( bediendeToUpdate ->
                            bediendeRepository.save(bediendeToUpdate.getUpdatedBediende())
                    );
                    break;

                case "bediendeDeleted":
                    LOG.debug("EVENT RECEIVED: A bediende was deleted!");
                    Optional<BediendeDeletedNotification> deletedBediende;

                    try {
                        deletedBediende = Optional.of(objectMapper.readValue(message.getBody(), BediendeDeletedNotification.class));
                    } catch (IOException e) {
                        deletedBediende = Optional.empty();
                    }

                    deletedBediende.ifPresent( bediendeToDelete ->
                            businessUnitRepository.delete(bediendeToDelete.getDeletedBediendeId())
                    );

                    break;


                default:
                    LOG.debug("EVENT RECEIVED: Unknown event!");
            }

        });

    }



//    private void reactToMessage(Function<BusinessUnit, BusinessUnit> repoFunction, byte[] message){
//        Optional<NewBusinessUnitNotification> buNotification;
//        System.out.println("debug1");
//
//        try {
//            buNotification = Optional.of(objectMapper.readValue(message, NewBusinessUnitNotification.class));
//        } catch (IOException e) {
//            buNotification = Optional.empty();
//        }
//
//        buNotification.ifPresent( buNot -> {
//            System.out.println("Saving business unit to the repo");
//            BusinessUnit buToSave = buNot.getBusinessUnit();
//            buToSave.setNaam("Hallo wereld :)");
//            repoFunction.apply(buNot.getBusinessUnit());
//            System.out.println("Saved to repo :)");
//        });
//    }


}
