package be.ac.umons.g06.model;

import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.event.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationRegister {

    private final Map<Account, List<Operation>> operationMap;

    public OperationRegister() {
        operationMap = new HashMap<>();
    }

    public List<Operation> getOperations(Account account) {
        return operationMap.get(account);
    }

    public void add(Account account, List<Operation> operations) {
        operationMap.put(account, operations);
    }
}
