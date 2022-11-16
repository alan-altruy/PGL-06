package be.ac.umons.g06.model.account;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountBuilderTest {

    private final LocalDate firstDate = LocalDate.of(1950, 10, 12);
    private final LocalDate secondDate = LocalDate.of(1980, 12, 24);
    private final Bank bank = new Bank("testSwift", "bankName");
    private final Customer customer = new Customer("testName", "testNrn", firstDate);
    private final Ownership ownership = new OwnershipBuilder()
            .type(OwnershipType.INDIVIDUAL)
            .owner(customer)
            .build();

    @Test()
    void build() {
        assertThrows(AssertionError.class, () -> new AccountBuilder().build());
        assertThrows(AssertionError.class, () -> new AccountBuilder().type(AccountType.CURRENT_ACCOUNT).build());
        assertThrows(AssertionError.class, () -> new AccountBuilder().ownership(ownership).build());
    }

    @Test()
    void build2() {
        Account account = new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .balance(505)
                .ownership(ownership)
                .creationDate(firstDate)
                .iban("BE37988228756928")
                .closingDate(secondDate)
                .bank(bank)
                .build();
        assertEquals(account.getType(), AccountType.CURRENT_ACCOUNT);
        assertEquals(account.getBalance(), 500 + 5);
        assertEquals(account.getBank(), bank);
        assertEquals(account.getCreationDate(), firstDate);
        assertEquals(account.getClosingDate(), secondDate);
        assertEquals(account.getIban(), "BE37988228756928");
        assertTrue(account.getOwnership().isCustomerInvolved(customer));
    }

    @Test()
    void build3() {
        assertThrows(AssertionError.class, () ->  new AccountBuilder()
                .type(AccountType.SAVINGS_ACCOUNT)
                .balance(505)
                .ownership(ownership)
                .creationDate(firstDate)
                .iban("BE37988228756928")
                .bank(bank)
                .build());
    }

    @Test()
    void build4() {
        Account account = new AccountBuilder()
                .type(AccountType.SAVINGS_ACCOUNT)
                .balance(505)
                .ownership(ownership)
                .creationDate(firstDate)
                .iban("BE37988228756928")
                .linkedAccountIban("BE15485624498730")
                .bank(bank)
                .build();
        assertEquals(account.getType(), AccountType.SAVINGS_ACCOUNT);
        assertEquals(account.getBalance(), 500 + 5);
        assertEquals(account.getBank(), bank);
        assertEquals(account.getCreationDate(), firstDate);
        assertEquals(account.getIban(), "BE37988228756928");
        assertEquals(account.getLinkedAccountIban(), "BE15485624498730");
        assertTrue(account.getOwnership().isCustomerInvolved(customer));
    }
}
