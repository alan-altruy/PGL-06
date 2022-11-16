package be.ac.umons.g06.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores a set of banks and allows to get a bank by its name or its id
 */
public class BankRegister {
    private final Set<Bank> banks;

    public BankRegister() {
        banks = new HashSet<>();
    }

    public BankRegister(Collection<Bank> banks) {
        this.banks = new HashSet<>(banks);
    }

    public void addBank(Bank bank) {
        banks.add(bank);
    }

    public Set<Bank> getAllBanks() {
        return banks;
    }

    public Set<String> getAllBanksNames() {
        return banks.stream().map(Bank::getName).collect(Collectors.toSet());
    }

    public Optional<Bank> getBankByName(String name) {
        for (Bank bank : banks)
            if (bank.getName().equals(name))
                return Optional.of(bank);
        return Optional.empty();
    }

    public Optional<Bank> getBankBySwift(String swift) {
        for (Bank bank : banks)
            if (bank.getId().equals(swift))
                return Optional.of(bank);
        return Optional.empty();
    }
}
