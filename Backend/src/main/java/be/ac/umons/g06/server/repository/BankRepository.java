package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.Bank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA interface that is used to interact with the Bank objects in the database
 */
@Repository
public interface BankRepository extends CrudRepository<Bank, String> {
    /**
     * Find a bank by name
     * @param name The name of the bank
     * @return The Bank if it exists
     */
    Optional<Bank> findByName(String name);
}
