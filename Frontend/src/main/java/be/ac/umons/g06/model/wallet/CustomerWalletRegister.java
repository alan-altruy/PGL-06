package be.ac.umons.g06.model.wallet;

import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.ownership.OwnershipInvolvement;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * WalletRegister optimized to store Wallets (i.e. group of accounts) such that all accounts in a wallet belongs to
 * the same bank.
 */
public class CustomerWalletRegister implements WalletRegister {

    private final HashMap<Bank, Wallet> wallets;

    public CustomerWalletRegister() {
        wallets = new HashMap<>();
    }

    @Override
    public void add(Account account) {
        if (!(account.getCreationDate() == null || account.isDisabled() || account.isClosed())) {
            if (!wallets.containsKey(account.getBank()))
                wallets.put(account.getBank(), new Wallet());
            wallets.get(account.getBank()).addAccount(account);
        }
    }

    @Override
    public Collection<Customer> getAllCustomers() {
        Collection<Customer> customers = new HashSet<>();
        for (Account account : getAllAccounts()) {
            for (OwnershipInvolvement involvement : account.getOwnership().getInvolvements())
                customers.add(involvement.getCustomer());
        }
        return customers;
    }

    @Override
    public Collection<Bank> getAllBanks() {
        return wallets.keySet();
    }

    @Override
    public Wallet getWalletByCustomer(Customer customer) {
        Wallet wallet = new Wallet();
        for (Account account : getAllAccounts()) {
            if (account.getOwnership().isCustomerInvolved(customer))
                wallet.addAccount(account);
        }
        return wallet;
    }

    @Override
    public Wallet getWalletByBank(Bank bank) {
        return wallets.get(bank);
    }

    @Override
    public Collection<Account> getAllAccounts() {
        Collection<Account> accounts = new HashSet<>();
        for (Wallet wallet : wallets.values())
            accounts.addAll(wallet.getAccounts());
        return accounts;
    }
}
