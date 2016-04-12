package company.tothepoint.repository;

import company.tothepoint.model.contract.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractRepository extends MongoRepository<Contract, String> {
}
