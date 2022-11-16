package be.ac.umons.g06.server.model.event;

import be.ac.umons.g06.server.model.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Account account;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    private int amount;

    @Enumerated
    @Column(nullable = false)
    private EventType type;

    @ManyToOne
    private Request originRequest;

    private String description;

    public Operation() {

    }

    public Operation(Account account, EventType type, int amount, LocalDateTime dateTime, String description) {
        this.amount = amount;
        this.account = account;
        this.type = type;
        this.dateTime = dateTime;
        this.description = description;
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

    /*
      Used only for json serialization
     */
    public String getAccountIban() {
        return account.getIban();
    }

    public int getAmount(){
        return this.amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }
}
