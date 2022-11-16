package be.ac.umons.g06.model.wallet;

import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountType;

import java.util.*;

/**
 * A wallet is here nothing more than a set of accounts. This is not limited to be the set of the accounts that a
 * customer has in a given institution.
 */
public class Wallet {

    private final HashMap<String, Account> accountsMap;

    Wallet() {
        accountsMap = new HashMap<>();
    }

    public void addAccount(Account account) {
        accountsMap.put(account.getIban(), account);
    }

    public Collection<Account> getAccounts() {
        return accountsMap.values();
    }

    public Account getAccountByIban(String iban) {
        return accountsMap.get(iban);
    }

    public Collection<Account> getAccountsWithType(AccountType type) {
        Collection<Account> result = new ArrayList<>();
        for (Account account : accountsMap.values()) {
            if (account.getType().equals(type))
                result.add(account);
        }
        return result;
    }
}
