package be.ac.umons.g06.model.account;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.ownership.Ownership;

import java.time.LocalDate;

public class AccountBuilder {

    private String iban;
    private AccountType type;
    private Bank bank;
    private LocalDate creationDate;
    private LocalDate closingDate;
    private int balance;
    private String linkedAccountIban;
    private Ownership ownership;
    private boolean disabled;

    public AccountBuilder() {

    }

    public Account build() {
        assert type != null && ownership != null && iban != null && bank != null && creationDate != null;
        assert !type.equals(AccountType.SAVINGS_ACCOUNT) || linkedAccountIban != null;
        return new Account(type, iban, bank, ownership, creationDate, closingDate, balance, disabled, linkedAccountIban);
    }

    public AccountBuilder type(AccountType type) {
        this.type = type;
        return this;
    }

    public AccountBuilder balance(int balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder iban(String iban) {
        this.iban = iban;
        return this;
    }

    public AccountBuilder disabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public AccountBuilder bank(Bank bank) {
        this.bank = bank;
        return this;
    }

    public AccountBuilder creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AccountBuilder closingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
        return this;
    }

    public AccountBuilder linkedAccountIban(String linkedAccountIban) {
        this.linkedAccountIban = linkedAccountIban;
        return this;
    }

    public AccountBuilder ownership(Ownership ownership) {
        this.ownership = ownership;
        return this;
    }
}
