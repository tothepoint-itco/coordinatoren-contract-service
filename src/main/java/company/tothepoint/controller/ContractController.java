package company.tothepoint.controller;

import company.tothepoint.model.bediende.Bediende;
import company.tothepoint.model.businessunit.BusinessUnit;
import company.tothepoint.model.contract.Contract;
import company.tothepoint.model.contract.ContractAggregate;
import company.tothepoint.repository.BediendeRepository;
import company.tothepoint.repository.BusinessUnitRepository;
import company.tothepoint.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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



    @RequestMapping(method = RequestMethod.GET, value = "/aggregated")
    public ResponseEntity<List<ContractAggregate>> getAllAggregatedContracts() {
        List<ContractAggregate> aggregatedContracts = contractRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getBediendeId().compareTo(o2.getBediendeId()))
                .map(this::aggregateContract)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(byContractStartDate)
                .sorted(((o1, o2) -> o1.getBediende().getFamilieNaam().compareTo(o2.getBediende().getFamilieNaam())))
                .sorted(((o1, o2) -> o1.getBediende().getVoorNaam().compareTo(o2.getBediende().getVoorNaam())))
                .collect(Collectors.toList());
        return new ResponseEntity<>(aggregatedContracts, HttpStatus.OK);
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
    public ResponseEntity<Contract> createContract(@Validated @RequestBody Contract contract) {

        Optional<BusinessUnit> businessUnitOption = Optional.ofNullable(businessUnitRepository.findOne(contract.getBusinessUnitId()));
        Optional<Bediende> bediendeOption = Optional.ofNullable(bediendeRepository.findOne(contract.getBediendeId()));

        return businessUnitOption.flatMap( businessUnit -> {
            return bediendeOption.map( bediende -> {
                return new ResponseEntity<>(contractRepository.save(contract), HttpStatus.CREATED);
            });
        }).orElse( new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable("id") String id, @Validated @RequestBody Contract contract) {
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

    private Optional<ContractAggregate> aggregateContract(Contract contract) {
        Optional<Bediende> bediendeOption = Optional.ofNullable(bediendeRepository.findOne(contract.getBediendeId()));

        return bediendeOption.map(bediende -> {
            return new ContractAggregate(contract, bediende);
        });
    }

    Comparator<ContractAggregate> byContractStartDate = (o1, o2) -> {
        if (o1.getContract().getStartDatum().isBefore(o2.getContract().getStartDatum()))
            return 1;
        else
            return -1;
    };
}
