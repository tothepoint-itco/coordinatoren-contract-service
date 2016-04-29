package company.tothepoint.repository;

import company.tothepoint.model.contract.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContractRepository extends MongoRepository<Contract, String> {
  List<Contract> findByBediendeId(String bediendeId);
}
