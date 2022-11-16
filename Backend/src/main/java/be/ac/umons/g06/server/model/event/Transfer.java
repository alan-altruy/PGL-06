package be.ac.umons.g06.server.model.event;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table (name = "transfers")
public class Transfer extends Request {

    private String structuredCommunication;

    private String communication;

    @ManyToOne
    @NotNull
    private Account destinationAccount;

    public Transfer() {

    }

    public Transfer(@NotNull Account originAccount, @NotNull Account destinationAccount, @NotNull Customer initiator, int amount, @NotNull String structuredCommunication, @NotNull String communication) {
        super(originAccount, EventType.TRANSFER, initiator, amount);
        this.destinationAccount = destinationAccount;
        this.communication = communication;
        this.structuredCommunication = structuredCommunication;
    }

    public Transfer(@NotNull Account originAccount, @NotNull Account destinationAccount, @NotNull Bank initiator, int amount, @NotNull String structuredCommunication, @NotNull String communication) {
        super(originAccount, EventType.TRANSFER, initiator, amount);
        this.destinationAccount = destinationAccount;
        this.communication = communication;
        this.structuredCommunication = structuredCommunication;
    }

    @JsonIgnore
    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public String getDestinationIban() {
        return destinationAccount.getIban();
    }

    public String getCommunication() {
        return communication;
    }

    public String getStructuredCommunication() {
        return structuredCommunication;
    }

    @Override
    protected List<Operation> getOperations() {
        account.setBalance(account.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        return List.of(
                new Operation(account, type, -amount, endDateTime, "{TRANSFER} {to} " + destinationAccount.getIban()),
                new Operation(destinationAccount, type, amount, endDateTime, "{TRANSFER} {from} " + account.getIban()));
    }
}
