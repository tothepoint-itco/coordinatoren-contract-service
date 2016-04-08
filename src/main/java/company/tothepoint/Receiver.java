package company.tothepoint;

import company.tothepoint.model.Contract;
import company.tothepoint.repository.ContractRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

class Receiver implements MessageListener {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void onMessage(Message message) {
        System.out.println("Received message: " + new String(message.getBody()));
    }


}
