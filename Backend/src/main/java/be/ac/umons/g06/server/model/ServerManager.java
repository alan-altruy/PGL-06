package be.ac.umons.g06.server.model;

import be.ac.umons.g06.server.service.AccountService;
import be.ac.umons.g06.server.service.BankService;
import be.ac.umons.g06.server.service.CustomerService;
import be.ac.umons.g06.server.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manager of the server
 */
@Component
public class ServerManager {
    /**
     * The BankService of the server
     */
    @Autowired
    private BankService bankService;
    /**
     * The CustomerService of the server
     */
    @Autowired
    private CustomerService customerService;
    /**
     * The AccountService of the server
     */
    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationService operationService;

    /**
     * Init the services of the server. (Put some date in the db to have something to test
     */
    public void work() {
        bankService.init();
        customerService.init();
        accountService.init();
        operationService.init();
    }
}
