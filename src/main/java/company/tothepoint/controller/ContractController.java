package company.tothepoint.controller;

import company.tothepoint.model.bediende.Bediende;
import company.tothepoint.model.businessunit.BusinessUnit;
import company.tothepoint.model.contract.Contract;
import company.tothepoint.repository.BediendeRepository;
import company.tothepoint.repository.BusinessUnitRepository;
import company.tothepoint.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private BediendeRepository bediendeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Contract>> getAllContracts(@RequestParam(required = false) String bediendeId) {
        //Optional<String> bediendeOption = Optional.ofNullable(bediendeId);

        if(bediendeId != null){
                    Optional<List<Contract>> contractOption = Optional.ofNullable(contractRepository.findByBediendeId(bediendeId));
                     return contractOption.map(contract ->
                            new ResponseEntity<List<Contract>>(contract, HttpStatus.OK)
                    ).orElse(
                            new ResponseEntity<List<Contract>>(HttpStatus.NOT_FOUND)
                    );
                }
        else{ return new ResponseEntity<List<Contract>>(contractRepository.findAll(), HttpStatus.OK);}
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

        Optional<BusinessUnit> businessUnitOption = Optional.ofNullable(businessUnitRepository.findOne(contract.getBusinessUnitId()));
        Optional<Bediende> bediendeOption = Optional.ofNullable(bediendeRepository.findOne(contract.getBediendeId()));

        return businessUnitOption.flatMap( businessUnit -> {
            return bediendeOption.map( bediende -> {
                return new ResponseEntity<>(contractRepository.save(contract), HttpStatus.CREATED);
            });
        }).orElse( new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable("id") String id, @RequestBody Contract contract) {
        Optional<Contract> existingContract = Optional.ofNullable(contractRepository.findOne(id));

        return existingContract.map(c ->
            {
                contract.setId(c.getId());
                return new ResponseEntity<>(contractRepository.save(contract), HttpStatus.OK);
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
