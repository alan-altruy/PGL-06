package be.ac.umons.g06.model.wallet;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountType;

import java.util.ArrayList;
import java.util.Collection;

public interface WalletRegister {

    void add(Account account);

    default void add(Collection<Account> accounts) {
        for (Account account : accounts)
            add(account);
    }

    Collection<Customer> getAllCustomers();

    Collection<Bank> getAllBanks();

    Wallet getWalletByCustomer(Customer customer);

    Wallet getWalletByBank(Bank bank);

    Collection<Account> getAllAccounts();

    default Collection<String> getAllCustomerNames() {
        Collection<String> names = new ArrayList<>();
        for (Customer customer: getAllCustomers())
            names.add(customer.getName());
        return names;
    }

    default Collection<String> getAllBanksNames() {
        Collection<String> banksNames = new ArrayList<>();
        for (Bank bank: getAllBanks())
            banksNames.add(bank.getName());
        return banksNames;
    }

    default Wallet getWalletByBankName(String bankName) {
        for (Bank bank: getAllBanks()) {
            if (bank.getName().equals(bankName))
                return getWalletByBank(bank);
        }
        return null;
    }

    default Collection<Account> getAllAccountsWithType(AccountType type) {
        Collection<Account> accounts = new ArrayList<>();
        for (Account account: getAllAccounts()) {
            if (account.getType().equals(type))
                accounts.add(account);
        }
        return accounts;
    }
}
