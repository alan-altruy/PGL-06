package be.ac.umons.g06.server.model.event;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The account that is concerned by the request
     */
    @ManyToOne
    @NotNull
    protected Account account;

    /**
     * When the request is created
     */
    @NotNull
    private LocalDateTime startDateTime;

    /**
     * When the request is closed (when the final decision is taken for the request)
     */
    protected LocalDateTime endDateTime;

    /**
     * The last time when a stakeholder has updated his decision concerning this request
     */
    private LocalDateTime lastUpdateDateTime;

    /**
     * The amount in euro-cents that will be added or removed to the account if the request is accepted. May be equal to
     * 0, if the request affects the status of the concerned account but not its balance.
     */
    protected int amount;

    /**
     * For each customer whose decision is needed, there is a CustomerDecision in this list. At the creation of the
     * request, all this CustomerDecision are in WAITING status, meaning that the concerned customer has not taken a
     * decision for this request.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<CustomerDecision> customersDecisions;

    /**
     * WAITING until the bank (the one that "hosts" the concerned account) accept or refuse the request. If not
     * WAITING, it cannot change.
     */
    @Column
    @NotNull
    private Decision bankDecision;

    /**
     * When the bank has taken his decision (null if no decision taken yet)
     */
    @Column
    private LocalDateTime bankDecisionDateTime;

    /**
     * WAITING until a stakeholder refuse the request or all stakeholders accept it. If the globalDecision is not
     * WAITING, it cannot change.
     */
    private Decision globalDecision;

    /**
     * The type of the request
     */
    @NotNull
    protected EventType type;

    /**
     * Empty constructor - necessary for JPA
     */
    public Request() {

    }

    public Request(Account account, EventType type, Bank initiator, int amount) {
        init(account, type, amount);
        bankDecision = Decision.ACCEPTED;
    }

    public Request(Account account, EventType type, Customer initiator, int amount) {
        init(account, type, amount);
        bankDecision = Decision.WAITING;

        customersDecisions.add(new CustomerDecision(initiator, Decision.ACCEPTED, startDateTime));
        for (Customer customer : account.getOwnership().getOwners()) {
            if (!customer.equals(initiator))
                customersDecisions.add(new CustomerDecision(customer, Decision.WAITING, startDateTime));
        }
    }

    private void init(Account account, EventType type, int amount) {
        startDateTime = LocalDateTime.now();
        lastUpdateDateTime = startDateTime;
        bankDecisionDateTime = startDateTime;
        this.amount = amount;
        this.account = account;
        this.type = type;

        globalDecision = Decision.WAITING;
        customersDecisions = new ArrayList<>();
    }

    @JsonProperty("id")
    public long getIdentifier() {
        return id;
    }

    public EventType getType() {
        return type;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public String getAccountIban() {
        return account.getIban();
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public List<CustomerDecision> getCustomersDecisions() {
        return customersDecisions;
    }

    public Decision getBankDecision() {
        return bankDecision;
    }

    public LocalDateTime getBankDecisionDateTime() {
        return bankDecisionDateTime;
    }


    /**
     * Get the global decision status of the request
     * @return The Final decision <li>ACCEPTED if the request is accepted</li>
     * <li>REJECTED if the request is not accepted</li>
     * <li>WAITING if not all users have made a decision yet</li>
     */
    public Decision getGlobalDecision() {
        return globalDecision;
    }

    public List<Operation> updateGlobalDecision() {
        if (globalDecision.equals(Decision.WAITING)) {
            Decision result = computeGlobalDecision();
            if (!result.equals(Decision.WAITING)) {
                globalDecision = result;
                endDateTime = LocalDateTime.now();
                if (globalDecision.equals(Decision.ACCEPTED))
                    return getOperations();
            }
        }
        return List.of();
    }

    protected List<Operation> getOperations() {
        if (type.equals(EventType.CREATION))
            account.setCreationDate(endDateTime.toLocalDate());
        account.setBalance(account.getBalance() + amount);
        if (type.equals(EventType.CLOSURE))
            account.setClosingDate(endDateTime.toLocalDate());
        return List.of(new Operation(account, type, amount, endDateTime, ""));
    }

    private Decision computeGlobalDecision() {
        List<Decision> decisions = new ArrayList<>();
        for (CustomerDecision customerDecision : customersDecisions)
            decisions.add(customerDecision.getDecision());
        decisions.add(bankDecision);
        Decision result = Decision.ACCEPTED;
        for (Decision decision : decisions) {
            if (decision.equals(Decision.REJECTED))
                return Decision.REJECTED;
            if (decision.equals(Decision.WAITING))
                result = Decision.WAITING;
        }
        return result;
    }

    /**
     * Update the decision of a Customer for this request. If the given Customer is not concerned by the request or if
     * the registered Decision for this customer is not WAITING (which means that the customer has already taken a
     * decision), nothing happens and the method return false.
     *
     * @param customer The Customer whose decision is updated
     * @param decision The new Decision given by this Customer for this request
     * @return true if a decision was updated, false else
     */
    public boolean updateCustomerDecision(Customer customer, Decision decision) {
        if (decision.equals(Decision.WAITING) || !globalDecision.equals(Decision.WAITING))
            return false;
        boolean updated = false;
        LocalDateTime now = LocalDateTime.now();
        for (CustomerDecision customerDecision : customersDecisions) {
            if (customerDecision.getCustomer().equals(customer) && customerDecision.getDecision().equals(Decision.WAITING)) {
                customerDecision.setDecision(decision, now);
                lastUpdateDateTime = now;
                updated = true;
            }
        }
        return updated;
    }

    /**
     * Update the decision of the bank for this request. If the registered Decision for this bank is not WAITING
     * (which means that the bank has already taken a decision), nothing happens and the method return false.
     *
     * @param bank the bank
     * @param decision The new Decision given by the bank for this request
     * @return true if the bank decision was updated, false else
     */
    public boolean updateBankDecision(Bank bank, Decision decision) {
        if (account.getBank().equals(bank) && bankDecision.equals(Decision.WAITING)) {
            bankDecision = decision;
            bankDecisionDateTime = LocalDateTime.now();
            lastUpdateDateTime = bankDecisionDateTime;
            return true;
        }
        return false;
    }
}
