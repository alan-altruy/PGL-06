package be.ac.umons.g06.model.event;

import be.ac.umons.g06.model.Customer;

import java.time.LocalDateTime;

public class CustomerDecision {
    private Customer customer;
    private LocalDateTime dateTime;
    private Decision decision;

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Decision getDecision() {
        return decision;
    }
}
