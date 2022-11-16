package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA interface that is used to interact with the Account objects in the database
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    Iterable<Account> findByBank(Bank bank);

}
