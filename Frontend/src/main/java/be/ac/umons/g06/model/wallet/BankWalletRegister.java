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
 * the same customer.
 */
public class BankWalletRegister implements WalletRegister {

    private final HashMap<Customer, Wallet> wallets;

    public BankWalletRegister() {
        wallets = new HashMap<>();
    }

    @Override
    public void add(Account account) {
        if (!(account.getCreationDate() == null || account.isDisabled() || account.isClosed())) {
            for (OwnershipInvolvement involvement : account.getOwnership().getInvolvements()) {
                Customer customer = involvement.getCustomer();
                if (!wallets.containsKey(customer))
                    wallets.put(customer, new Wallet());
                wallets.get(customer).addAccount(account);
            }
        }
    }

    @Override
    public Collection<Bank> getAllBanks() {
        Collection<Bank> banks = new HashSet<>();
        for (Account account : getAllAccounts())
            banks.add(account.getBank());
        return banks;
    }

    @Override
    public Collection<Customer> getAllCustomers() {
        return wallets.keySet();
    }

    @Override
    public Wallet getWalletByCustomer(Customer customer) {
        return wallets.get(customer);
    }

    @Override
    public Wallet getWalletByBank(Bank bank) {
        Wallet wallet = new Wallet();
        for (Account account : getAllAccounts()) {
            if (account.getBank().equals(bank))
                wallet.addAccount(account);
        }
        return wallet;
    }

    @Override
    public Collection<Account> getAllAccounts() {
        Collection<Account> accounts = new HashSet<>();
        for (Wallet wallet : wallets.values())
            accounts.addAll(wallet.getAccounts());
        return accounts;
    }
}
