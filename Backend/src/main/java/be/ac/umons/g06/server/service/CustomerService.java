package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * CustomerService
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    PasswordEncoder encoder;

    /**
     * Get customer with NRN
     * @param natRegNumber The national register number
     * @return The customer if it exists
     */
    public Optional<Customer> getCustomer(final String natRegNumber) {
        return repository.findById(natRegNumber);
    }

    /**
     * Get customer with name
     * @param name The name of the customer
     * @return The customer if it exists
     */
    public Optional<Customer> getCustomerByName(final String name) {
        return repository.findByName(name);
    }

    /**
     * Get all customers
     * @return List of customers
     */
    public Iterable<Customer> getCustomers() {
        return repository.findAll();
    }

    /**
     * Initialize Customers
     */
    public void init() {
        Collection<Customer> customers = new ArrayList<>();
        customers.add(new Customer("R000000KM", "Karl Marx", encoder.encode("capital"), LocalDate.of(1818, 5, 5)));
        customers.add(new Customer("R000001FE", "Friedrich Engels", encoder.encode("capital"), LocalDate.of(1820, 11, 28)));
        customers.add(new Customer("R000002AG", "Antonio Gramsci", encoder.encode("capital"), LocalDate.of(1891, 1, 22)));
        customers.add(new Customer("F000003LL", "Lucky Luke", encoder.encode("fiction"), LocalDate.of(2010, 4, 24)));
        customers.add(new Customer("R000004VI", "Vladimir Illitch", encoder.encode("capital"), LocalDate.of(1870, 4, 10)));
        customers.add(new Customer("R000005RL", "Rosa Luxemburg", encoder.encode("capital"), LocalDate.of(1871, 3, 5)));
        customers.add(new Customer("R000006LT", "Léon Trotski", encoder.encode("capital"), LocalDate.of(1879, 10, 26)));
        repository.saveAll(customers);
    }

    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}
