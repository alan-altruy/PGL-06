package be.ac.umons.g06.server.model.event;

import be.ac.umons.g06.server.model.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table (name = "customers_decisions")
public class CustomerDecision {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Customer customer;

    @NotNull
    private Decision decision;

    @NotNull
    private LocalDateTime dateTime;

    public CustomerDecision() {

    }

    public CustomerDecision(Customer customer, Decision decision, LocalDateTime dateTime) {
        this.customer = customer;
        this.decision = decision;
        this.dateTime = dateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision, LocalDateTime dateTime) {
        this.decision = decision;
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
