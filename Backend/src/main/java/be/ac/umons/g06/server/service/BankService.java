package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    PasswordEncoder encoder;

    /**
     * Get a bank from a SWIFT
     * @param swift The SWIFT of the bank
     * @return Optional<Bank>
     */
    public Optional<Bank> getBank(final String swift) {
        return bankRepository.findById(swift);
    }

    /**
     * Get a bank from its name
     * @param name The name of the bank
     * @return Optional<Bank>
     */
    public Optional<Bank> findBankByName(String name) {
        return bankRepository.findByName(name);
    }

    /**
     * Get all banks
     * @return List of banks
     */
    public Iterable<Bank> getBanks() {
        return bankRepository.findAll();
    }

    /**
     * Initialize banks
     */
    public void init() {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank("ING-01", "ING", encoder.encode("azerty2022")));
        banks.add(new Bank("Belfius-02", "Belfius", encoder.encode("azerty2022")));
        banks.add(new Bank("HSBC-03", "HSBC", encoder.encode("azerty2022")));
        banks.add(new Bank("CBC-04", "CBC", encoder.encode("azerty2022")));
        banks.add(new Bank("AXA-05", "AXA", encoder.encode("azerty2022")));
        banks.add(new Bank("BPost-06", "BPost bank", encoder.encode("azerty2022")));
        banks.add(new Bank("Deutsche-07", "Deutsche bank", encoder.encode("azerty2022")));
        bankRepository.saveAll(banks);
    }

    /**
     * Save a bank
     * @param bank The saved bank
     */
    public void save(Bank bank) {
        bankRepository.save(bank);
    }
}
