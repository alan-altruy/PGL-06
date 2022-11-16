package be.ac.umons.g06.model.event;

public abstract class Event {

    /**
     * Technical identifier of the event.
     */
    private final long id;

    /**
     * The iban of the account that is concerned by this operation.
     */
    protected String accountIban;

    /**
     * The type of the event.
     */
    protected EventType type;

    /**
     * The amount of money added to or taken from the account. Equal to 0 if the operation has not affected the balance
     * of the concerned account.
     */
    protected int amount;

    protected Event(long id, String accountIban, EventType type, int amount) {
        this.id = id;
        this.accountIban = accountIban;
        this.type = type;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getAccountIban() {
        return accountIban;
    }

    public EventType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public abstract String getDescription();
}
