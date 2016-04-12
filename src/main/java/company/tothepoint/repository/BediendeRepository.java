package company.tothepoint.repository;

import company.tothepoint.model.bediende.Bediende;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BediendeRepository extends MongoRepository<Bediende, String> {
}
