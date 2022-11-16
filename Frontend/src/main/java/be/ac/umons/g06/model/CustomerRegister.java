package be.ac.umons.g06.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Stores a set of Customer
 */
public class CustomerRegister {

    private final Set<Customer> customers;

    public CustomerRegister() {
        customers = new HashSet<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Optional<Customer> findCustomer(String id) {
        for (Customer customer: customers) {
            if (customer.getName().equals(id) || customer.getId().equals(id))
                return Optional.of(customer);
        }
        return Optional.empty();
    }
}
