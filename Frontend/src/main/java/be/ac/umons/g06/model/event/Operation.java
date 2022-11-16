package be.ac.umons.g06.model.event;

import java.time.LocalDateTime;

/**
 * An operation is a performed action that has modified the status or the balance of an account. In the main use case
 * this object is directly deserialized from a json received from the server.
 */
public class Operation extends Event {

    /**
     * The moment when the operation was performed
     */
    private final LocalDateTime dateTime;

    /**
     * A string that describes the operation
     */
    private final String description;

    public Operation(Long id, EventType type, int amount, LocalDateTime dateTime, String accountIban, String description) {
        super(id, accountIban, type, amount);
        this.dateTime = dateTime;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    };

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
