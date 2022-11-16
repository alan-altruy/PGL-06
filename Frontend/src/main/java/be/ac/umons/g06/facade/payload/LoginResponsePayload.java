package be.ac.umons.g06.facade.payload;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.User;

/**
 * The POJO that matches the json which is the body of the response to a successful login request sent to the server.
 */
public class LoginResponsePayload {

    private String type;
    private String token;
    private Customer customer;
    private Bank bank;

    public String getToken() {
        return token;
    }

    public User getUser() {
        if (customer == null)
            return bank;
        return customer;
    }
}
