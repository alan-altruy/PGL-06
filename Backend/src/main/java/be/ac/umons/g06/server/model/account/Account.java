package be.ac.umons.g06.server.model.account;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.ownership.Ownership;
import be.ac.umons.g06.server.service.AccountService;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

/**
 * Account
 */
@Entity
@Table(name = "accounts")
public class Account {
    /**
     * IBAN of the account
     */
    @Id
    private String iban;
    /**
     * Type of the account
     */
    @NotNull(message = "Account type cannot be null")
    private AccountType type;
    /**
     * Account creation date
     */
    @Column(name = "creation_date")
    private LocalDate creationDate;
    /**
     * Account closure date
     */
    @Column(name = "closing_date")
    private LocalDate closingDate;
    /**
     * Account ownership
     */
    @OneToOne(cascade = CascadeType.ALL)
    private Ownership ownership;
    /**
     * Bank where the account is located
     */
    @ManyToOne
    @NotNull(message = "Bank cannot be null")
    private Bank bank;
    /**
     * Iban of the linked account
     */
    private String linkedAccountIban;
    /**
     * Account balance in euro-cents
     */
    private int balance;

    /**
     * Empty constructor necessary for the JPA
     */
    public Account (){

    }

    /**
     * Constructor used by the AccountBuilder
     * @param type Type of the account
     * @param iban IBAN of the account
     * @param bank Bank where the account is located
     * @param ownership Account ownership
     */
    Account(AccountType type, String iban, Bank bank, Ownership ownership) {
        this.type = type;
        this.iban = iban;
        this.bank = bank;
        setOwnership(ownership);
    }

    /**
     * Get the IBAN of the account
     * @return IBAN of the account
     */
    @NotNull
    @Pattern(regexp = "[A-Z]{2}\\d{14}")
    public String getIban() {
        return iban;
    }

    /**
     * Get the type of the account
     * @return Type of the account
     */
    public AccountType getType() {
        return type;
    }

    /**
     * Get account ownership
     * @return The ownership of the account
     */
    public Ownership getOwnership() {
        return ownership;
    }

    /**
     * Set account ownership
     * @param ownership The ownership of the account
     */
    public void setOwnership(Ownership ownership) {
        assert ownership != null;
        this.ownership = ownership;
        ownership.setAccount(this);
    }

    /**
     * Get the bank where the account is located
     * @return The bank where the account is located
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Get the creation date of the account
     * @return The creation date of the account
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Set the creation date of the account
     * @param creationDate The creation date of the account
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get the closure date of the account
     * @return The closure date of the account (LocalDate)
     */
    public LocalDate getClosingDate() {
        return closingDate;
    }

    /**
     * Set the closure date of the account
     * @param closingDate The closure date of the account
     */
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Get the balance of the account
     * @return The balance of the account
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Set the balance of the account
     * @param balance The balance of the account
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Get the linked account IBAN
     * @return The linked account IBAN (String)
     */
    @Pattern(regexp = "[A-Z]{2}\\d{14}")
    public String getLinkedAccountIban() {
        return linkedAccountIban;
    }

    /**
     * Set the linked account IBAN
     * @param linkedAccountIban The linked account IBAN
     */
    public void setLinkedAccountIban(String linkedAccountIban) {
        this.linkedAccountIban = linkedAccountIban;
    }

    /**
     * Ensure that this account is linked to an existing "current account" if it is a "savings account"
     * @param accountService AccountService used to check that the linkedAccountIban attribute refers to an existing
     *                       account
     * @return true if the account is not a savings account or if its linkedAccountIban attribute refers to an existing
     *         account, false else
     */
    @JsonIgnore
    public boolean checkLinkedAccount(AccountService accountService) {
        if (type.equals(AccountType.SAVINGS_ACCOUNT)) {
            if (linkedAccountIban != null && accountService.getAccount(linkedAccountIban).isPresent()) {
                Account linkedAccount = accountService.getAccount(linkedAccountIban).get();
                return linkedAccount.type.equals(AccountType.CURRENT_ACCOUNT)
                        && linkedAccount.bank.equals(bank);
            }
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    /**
     * Generate a valid iban
     * @return A String that is a valid iban for an account
     */
    public static String generateIban(){
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++)
            result.append(random.nextInt(10));
        long checksum = 98 - Long.parseLong(result.toString() + 111400) % 97;
        if (checksum < 10)
            result.insert(0, "BE" + "0" + checksum);
        else
            result.insert(0, "BE" + checksum);
        return result.toString();
    }
}
