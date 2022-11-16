package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.event.Operation;
import be.ac.umons.g06.server.model.event.EventType;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Get operations of an account
     * @param account The concerned account
     * @return The list of operations of the account
     */
    public Iterable<Operation> findByAccount(Account account) {
        return operationRepository.findByAccount(account);
    }

    /**
     * Return all the operations that concern this account and that were created after the specified LocalDateTime
     * @param account the account
     * @param dateTime the dateTime
     */
    public Iterable<Operation> findByAccount(Account account, LocalDateTime dateTime) {
        List<Operation> operations = new ArrayList<>();
        for (Operation operation : findByAccount(account))
            if (operation.getDateTime().compareTo(dateTime) >= 0)
                operations.add(operation);
        return operations;
    }

    /**
     * Find an Operation
     * @param id The ID of the operation
     * @return The operation if it exists
     */
    public Optional<Operation> findById(Long id){
        return operationRepository.findById(id);
    }

    /**
     * Save an operation
     * @param operation The operation
     */
    public Operation save(Operation operation) {
        accountService.saveAccount(operation.getAccount());
        return operationRepository.save(operation);
    }

    public void init() {
        for (Account account : accountService.findAll())
            operationRepository.saveAll(getRandomExample(account));
    }

    private static List<Operation> getRandomExample(Account account) {
        List<Operation> list = new ArrayList<>();
        Random random = new Random();
        LocalDate currentDate = account.getCreationDate();
        LocalDate endDate = account.getClosingDate() == null ? LocalDate.now() : account.getClosingDate();

        list.add(new Operation(account, EventType.CREATION, 0, currentDate.atStartOfDay(), "{account_creation}"));

        while(currentDate.compareTo(endDate) < 0) {
            int amount = Math.random() < 0.5 ? random.nextInt(100000) : -random.nextInt(100000);
            list.add(new Operation(account, EventType.TRANSFER, amount, currentDate.atStartOfDay().plusHours(random.nextInt(24)), "{transfer}"));
            currentDate = currentDate.plusDays(random.nextInt(6) + 1);
        }
        return list;
    }
}
