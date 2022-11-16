package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.User;
import be.ac.umons.g06.server.repository.BankRepository;
import be.ac.umons.g06.server.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserService
 */
@Service
public class UserService {
    /**
     * Object of CustomerRepository
     */
    @Autowired
    CustomerRepository customerRepository;
    /**
     * Object of BankRepository
     */
    @Autowired
    BankRepository bankRepository;

    /**
     * Find a User by username
     * @param username The username of the User
     * @return Customer or Bank if it exists
     */
    public Optional<? extends User> findByUsername(String username) {
        String[] arr = username.split("=", 2);
        String type = arr[0];
        String id = arr[1];

        if (type.equals("BANK")) {
            return bankRepository.findById(id);
        } else if (type.equals("CUSTOMER")) {
            return customerRepository.findById(id);
        }
        return Optional.empty();
    }
}
