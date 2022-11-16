package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.event.Request;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA interface that is used to interact with the Request objects in the database
 */
@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

    Iterable<Request> findByAccount(Account account);

}
