package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.account.AccountBuilder;
import be.ac.umons.g06.server.model.account.AccountType;
import be.ac.umons.g06.server.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.server.model.ownership.OwnershipType;
import be.ac.umons.g06.server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private BankService bankService;

    @Autowired
    private CustomerService customerService;

    /**
     * Put some examples in DB to have some cases in the dev and test processes
     */
    public void init() {
        Customer marx = customerService.getCustomer("R000000KM").get();
        Customer engels = customerService.getCustomer("R000001FE").get();
        Customer luke = customerService.getCustomer("F000003LL").get();
        Bank ing = bankService.findBankByName("ING").get();
        Bank belfius = bankService.findBankByName("Belfius").get();

        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE50812535323418")
                .balance(5045825)
                .bank(ing)
                .creationDate(LocalDate.of(2013, 3, 30))
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.INDIVIDUAL)
                        .owner(marx)
                        .build())
                .build());

        accounts.add(new AccountBuilder()
                .type(AccountType.TERM_ACCOUNT)
                .bank(ing)
                .iban("BE07978547171366")
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.JOIN)
                        .owner(marx)
                        .owner(engels)
                        .build())
                .creationDate(LocalDate.of(2015, 11, 15))
                .balance(365245)
                .build());

        accounts.add(new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .balance(3535)
                .bank(belfius)
                .iban("BE58557747844879")
                .creationDate(LocalDate.of(2014, 4, 1))
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.INDIVIDUAL)
                        .owner(marx)
                        .build())
                .build());

        accounts.add(new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .balance(365421)
                .bank(belfius)
                .iban("BE86233242334750")
                .creationDate(LocalDate.of(2016, 5, 28))
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.YOUNG)
                        .young(luke)
                        .supervisor(marx)
                        .build())
                .build());

        accounts.add(new AccountBuilder()
                .type(AccountType.SAVINGS_ACCOUNT)
                .linkedAccount(accounts.get(2))
                .balance(54124541)
                .bank(belfius)
                .iban("BE36978442994881")
                .creationDate(LocalDate.of(2016, 5, 27))
                .ownership( new OwnershipBuilder()
                        .type(OwnershipType.INDIVIDUAL)
                        .owner(marx)
                        .build())
                .build());

        repository.saveAll(accounts);
    }

    public Iterable<Account> findByBank(Bank bank) {
        return repository.findByBank(bank);
    }

    public Iterable<Account> findAll() {
        return repository.findAll();
    }

    public Account saveAccount(Account account) {
        return repository.save(account);
    }

    public Optional<Account> getAccount(String iban) {
        return repository.findById(iban);
    }

    /**
     * Generate an unused valid iban
     * @return A String that is an unused valid iban
     */
    public String generateUnusedIban() {
        String iban = "";
        do {
            iban = Account.generateIban();
        } while (repository.existsById(iban));
        return iban;
    }
}
