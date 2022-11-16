package be.ac.umons.g06.server.model.account;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.ownership.Ownership;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * Account builder
 */
public class AccountBuilder {
    /**
     * IBAN of the account
     */
    private String iban;
    /**
     * Type of the account
     */
    private AccountType type;
    /**
     * Bank where the account is located
     */
    private Bank bank;
    /**
     * Creation date of the account
     */
    private LocalDate creationDate;
    /**
     * Closure date of the account
     */
    private LocalDate closingDate;
    /**
     * Balance of the account in euro-cents
     */
    private int balance;
    /**
     * Account object of the linked account
     */
    private Account linkedAccount;
    /**
     * Ownership of the account
     */
    private Ownership ownership;

    /**
     * Constructor of AccountBuilder
     */
    public AccountBuilder() {
        balance = 0;
        creationDate = LocalDate.now();
    }

    /**
     * Build the account
     * @return The account
     */
    public Account build() {
        assert type != null && ownership != null && iban != null && bank != null && creationDate != null;
        Account account = new Account(type, iban, bank, ownership);

        if (type.equals(AccountType.SAVINGS_ACCOUNT)) {
            assert linkedAccount != null && linkedAccount.getType().equals(AccountType.CURRENT_ACCOUNT);
            account.setLinkedAccountIban(linkedAccount.getIban());
        }

        account.setCreationDate(creationDate);
        account.setClosingDate(closingDate);
        account.setBalance(balance);
        return account;
    }

    /**
     * Set the type of the account
     * @param type Type of the account
     * @return AccountBuilder
     */
    public AccountBuilder type(@NotNull AccountType type) {
        this.type = type;
        return this;
    }

    /**
     * Set the balance of the account
     * @param balance Balance of the account
     * @return AccountBuilder
     */
    public AccountBuilder balance(int balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Set the IBAN of the account
     * @param iban IBAN of the account
     * @return AccountBuilder
     */
    public AccountBuilder iban(@Pattern(regexp = "[A-Z]{2}\\d{14}") String iban) {
        this.iban = iban;
        return this;
    }

    /**
     * Set the bank where the account is located
     * @param bank The bank where the account is located
     * @return AccountBuilder
     */
    public AccountBuilder bank(@NotNull Bank bank) {
        this.bank = bank;
        return this;
    }

    /**
     * Set the creation date of the account
     * @param creationDate The creation date of the account
     * @return AccountBuilder
     */
    public AccountBuilder creationDate(@NotNull LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    /**
     * Set the closure date of the account
     * @param closingDate The closure date of the account
     * @return AccountBuilder
     */
    public AccountBuilder closingDate(@NotNull LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    /**
     * Set the Account linked to the account
     * @param linkedAccount The Account linked to the account
     * @return AccountBuilder
     */
    public AccountBuilder linkedAccount(@NotNull Account linkedAccount) {
        assert linkedAccount.getType().equals(AccountType.CURRENT_ACCOUNT);
        this.linkedAccount = linkedAccount;
        return this;
    }

    /**
     * Set the ownership ogf the account
     * @param ownership The ownership of the account
     * @return AccountBuilder
     */
    public AccountBuilder ownership(@NotNull Ownership ownership) {
        this.ownership = ownership;
        return this;
    }
}
