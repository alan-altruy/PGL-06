package be.ac.umons.g06.model.wallet;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class WalletRegisterTest {

    private final Bank bank1 = new Bank("swiftTest1", "nameTest1");
    private final Bank bank2 = new Bank("swiftTest2", "nameTest2");

    private final Customer customer1 = new Customer("Joe", "2525", LocalDate.of(2000,1,1));
    private final Customer customer2 = new Customer("John", "1234", LocalDate.of(1950,10,20));
    private final Customer customer3 = new Customer("Jack", "0000", LocalDate.of(1990,5,10));

    private final Account account1 = new AccountBuilder()
            .type(AccountType.CURRENT_ACCOUNT)
            .ownership(new OwnershipBuilder().type(OwnershipType.INDIVIDUAL).owner(customer1).build())
            .creationDate(LocalDate.of(2000,1,2))
            .iban("BE37988228756928")
            .bank(bank1)
            .build();


    private final Account account2 = new AccountBuilder()
            .type(AccountType.CURRENT_ACCOUNT)
            .ownership(new OwnershipBuilder().type(OwnershipType.INDIVIDUAL).owner(customer2).build())
            .creationDate(LocalDate.of(2000,1,2))
            .iban("BE37988228756929")
            .bank(bank2)
            .build();

    protected abstract WalletRegister getWalletRegister();

    private WalletRegister initWalletRegister() {
        WalletRegister walletRegister = getWalletRegister();
        walletRegister.add(account1);
        walletRegister.add(account2);
        return walletRegister;
    }

    private boolean compareSets(Collection<?> col1, Collection<?> col2){
        return col1.containsAll(col2) && col2.containsAll(col1);
    }

    void AddAccount() {
        WalletRegister walletRegister = getWalletRegister();
        assertEquals(0,walletRegister.getAllAccounts().size());
        walletRegister.add(account1);
        assertEquals(1,walletRegister.getAllAccounts().size());
        assertEquals(1,walletRegister.getAllBanks().size());
    }

    @Test
    void getAllCustomers() {
        WalletRegister walletRegister = initWalletRegister();
        assertEquals(Set.of(customer1,customer2), walletRegister.getAllCustomers());
    }

    @Test
    void getAllBanks() {
        WalletRegister walletRegister = initWalletRegister();
        assertEquals(Set.of(bank1,bank2), walletRegister.getAllBanks());
    }

    @Test
    void getWalletByCustomer() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getWalletByCustomer(customer1).getAccounts(),Set.of(account1)));
        assertTrue(compareSets(walletRegister.getWalletByCustomer(customer2).getAccounts(),Set.of(account2)));
    }

    @Test
    void getAllAccounts() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getAllAccounts(), Set.of(account1,account2)));
    }

    @Test
    void getAllCustomerNames() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getAllCustomerNames(), Set.of("Joe", "John")));
    }

    @Test
    void getAllBanksNames() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getAllBanksNames(), Set.of("nameTest1", "nameTest2")));
    }

    @Test
    void getWalletByBankName() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getWalletByBankName("nameTest1").getAccounts(), Set.of(account1)));
    }

    @Test
    void getAllAccountsWithType() {
        WalletRegister walletRegister = initWalletRegister();
        assertTrue(compareSets(walletRegister.getAllAccountsWithType(AccountType.CURRENT_ACCOUNT), Set.of(account1,account2)));
        assertTrue(compareSets(walletRegister.getAllAccountsWithType(AccountType.SAVINGS_ACCOUNT), Set.of()));
    }
}
