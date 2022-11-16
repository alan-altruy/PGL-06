package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.ownership.Ownership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA interface that is used to interact with the Ownership objects in the database
 */
@Repository
public interface OwnershipRepository extends CrudRepository<Ownership, Long> {
}
