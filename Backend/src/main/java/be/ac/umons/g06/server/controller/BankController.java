package be.ac.umons.g06.server.controller;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Class allowing database bank control
 */
@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET})
@PreAuthorize("hasRole('BANK') or hasRole('CUSTOMER')")
public class BankController {

    @Autowired
    private BankService bankService;

    /**
     * Get all banks
     * @return Banks of the database
     */
    @GetMapping("/banks")
    public Iterable<Bank> getBanks() {
        return bankService.getBanks();
    }

    /**
     * Get one bank
     * @param swift The swift of the bank you want to get
     * @return The SWIFT correspondent bank
     */
    @GetMapping("/bank/{swift}")
    public Bank getBank(@PathVariable("swift") final String swift) {
        Optional<Bank> optBank = bankService.findBankByName(swift);
        return optBank.orElse(null);
    };
}