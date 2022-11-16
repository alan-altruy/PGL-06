package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.event.Operation;
import be.ac.umons.g06.server.model.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA interface that is used to interact with the Operation objects in the database
 */
@Repository
public interface OperationRepository extends CrudRepository<Operation, Long> {

    Iterable<Operation> findByAccount(Account account);

}
