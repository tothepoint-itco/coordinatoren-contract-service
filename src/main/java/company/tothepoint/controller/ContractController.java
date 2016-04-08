package company.tothepoint.controller;

import company.tothepoint.model.Contract;
import company.tothepoint.repository.ContractRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business-units")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Contract>> getAllContracts() {
        return new ResponseEntity<>(contractRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable("id") String id) {
        Optional<Contract> contractOption = Optional.ofNullable(contractRepository.findOne(id));

        return contractOption.map(contract->
            new ResponseEntity<>(contract, HttpStatus.OK)
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        return new ResponseEntity<>(contractRepository.save(contract), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable("id") String id, @RequestBody Contract contract) {
        Optional<Contract> existingContract = Optional.ofNullable(contractRepository.findOne(id));

        return existingContract.map(bu ->
            {
                bu.setNaam(contract.getNaam());
                return new ResponseEntity<>(contractRepository.save(bu), HttpStatus.OK);
            }
        ).orElse(
            new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Contract> deleteContract(@PathVariable("id") String id) {
        if (contractRepository.exists(id)) {
            contractRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
