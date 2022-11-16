package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.event.Operation;
import be.ac.umons.g06.server.model.event.Request;
import be.ac.umons.g06.server.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OperationService operationService;

    /**
     * An Iterable on all the requests that concern a given account
     * @param account The account
     * @return Iterable<Request> object
     */
    public Iterable<Request> findByAccount(Account account) {
        return requestRepository.findByAccount(account);
    }

    public Optional<Request> findRequest(Long id){
        return requestRepository.findById(id);
    }

    public Request saveRequest(Request request) {
        for (Operation operation : request.updateGlobalDecision())
            operationService.save(operation);
        return requestRepository.save(request);
    }

    /**
     * Return all the requests that concern this account and that were updated after the specified lastUpdate
     * LocalDateTime
     * @param account
     * @param lastUpdate
     */
    public Iterable<Request> findByAccount(Account account, LocalDateTime lastUpdate) {
        List<Request> requests = new ArrayList<>();
        for (Request request : findByAccount(account))
            if (request.getLastUpdateDateTime().compareTo(lastUpdate) >= 0)
                requests.add(request);
        return requests;
    }

    public Iterable<Request> getCustomerRequests(Customer customer, LocalDateTime lastUpdateDateTime) {
        List<Request> requests = new ArrayList<>();
        for (Request request : requestRepository.findAll())
            if (request.getLastUpdateDateTime().compareTo(lastUpdateDateTime) >= 0 && request.getAccount().getOwnership().isCustomerInvolved(customer))
                requests.add(request);
        return requests;
    }

    public Iterable<Request> getBankRequests(Bank bank, LocalDateTime lastUpdateDateTime) {
        List<Request> requests = new ArrayList<>();
        for (Request request : requestRepository.findAll())
            if (request.getLastUpdateDateTime().compareTo(lastUpdateDateTime) >= 0 && request.getAccount().getBank().equals(bank))
                requests.add(request);
        return requests;
    }
}
