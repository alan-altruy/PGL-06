package be.ac.umons.g06.model.account;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.ownership.Ownership;

import java.time.LocalDate;
import java.util.Objects;

public class Account {
    private final AccountType type;
    private final String iban;
    private final Bank bank;
    private final LocalDate creationDate;
    private final LocalDate closingDate;
    private final int balance;
    private final Ownership ownership;
    private boolean disabled;
    private final String linkedAccountIban;

    Account(AccountType type, String iban, Bank bank, Ownership ownership, LocalDate creationDate, LocalDate closingDate, int balance, boolean disabled, String linkedAccountIban) {
        assert type != null && iban != null && !iban.equals("") && bank != null;
        this.type = type;
        this.iban = iban;
        this.bank = bank;
        this.ownership = ownership;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
        this.balance = balance;
        this.disabled = disabled;
        this.linkedAccountIban = linkedAccountIban;
    }

    public AccountType getType() {
        return type;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public String getIban() {
        return iban;
    }

    public Bank getBank() {
        return bank;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public boolean isClosed() {
        return closingDate != null;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public int getBalance() {
        return balance;
    }

    public String getDisplayedBalance() {
        return String.valueOf((double)balance/100);
    }

    public String getLinkedAccountIban() {
        return linkedAccountIban;
    }

    public String prettyPrintIban() {
        return prettyPrintIban(iban);
    }

    public static String prettyPrintIban(String iban) {
        return iban.replaceAll("(.{" + "4" + "})", "$1 ").trim();
    }

    public static boolean isValidIban(String iban) {
        iban = iban.replaceAll(" ", "");
        if (iban.matches("[A-Z]{2}\\d{14}")) {
            String start = String.valueOf((int) iban.charAt(0) - 55);
            start += String.valueOf((int) iban.charAt(1) - 55);
            return Long.parseLong(iban.substring(4, 16) + start + iban.substring(2, 4)) % 97 == 1;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Account other = (Account) o;
        return iban.equals(other.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }
}
