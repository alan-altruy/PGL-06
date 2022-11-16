package be.ac.umons.g06.model.event;

import be.ac.umons.g06.model.Customer;

import java.time.LocalDateTime;
import java.util.List;

public class Request extends Event {

    private List<CustomerDecision> customersDecisions;
    private Decision bankDecision;
    private LocalDateTime startDateTime;
    private LocalDateTime bankDecisionDateTime;
    private LocalDateTime lastUpdateDateTime;
    private Decision globalDecision;
    private String structuredCommunication;
    private String freeCommunication;
    private String destinationIban;

    protected Request(long id, String accountIban, EventType type, int amount) {
        super(id, accountIban, type, amount);
    }

    public Decision getGlobalDecision() {
        return globalDecision;
    }

    public Decision getBankDecision() {
        return bankDecision;
    }

    public String getStructuredCommunication() {
        return structuredCommunication;
    }

    public Decision getCustomerDecision(Customer customer) {
        for (CustomerDecision customerDecision : customersDecisions)
            if (customerDecision.getCustomer().equals(customer))
                return customerDecision.getDecision();
        return null;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return globalDecision.equals(Decision.WAITING) ? null : lastUpdateDateTime;
    }

    @Override
    public String getDescription() {
        return "";
    }
}