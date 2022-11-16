package be.ac.umons.g06.server.repository;

import be.ac.umons.g06.server.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA interface that is used to interact with the Customer objects in the database
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    /**
     * Find a Customer by ID
     * @param id The ID of the Customer
     * @return The customer if it exists
     */
    Optional<Customer> findById(String id);

    /**
     * Checks if a Customer exists
     * @param name The name of the Customer
     * @return A boolean Object <li>True if the Customer exists</li>
     * <li>False if the Customer does not exist</li>
     */
    boolean existsByName(String name);

    /**
     * Find a Customer by name
     * @param name The name of the Customer
     * @return The Customer if it exists
     */
    Optional<Customer> findByName(String name);
}
