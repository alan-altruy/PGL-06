package be.ac.umons.g06.session;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.facade.RestClient;
import be.ac.umons.g06.facade.SafeClient;
import be.ac.umons.g06.facade.OfflineClient;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.model.wallet.WalletRegister;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class Session {

    protected ViewsManager viewsManager;
    private BankRegister bankRegister;
    private CustomerRegister customerRegister;
    private OperationRegister operationRegister;

    protected RestClient restClient;
    protected boolean offlineMode;

    protected Session(ViewsManager manager) {
        viewsManager = manager;
        initClient();
    }

    private void initClient() {
        customerRegister = new CustomerRegister();
        operationRegister = new OperationRegister();
        if (offlineMode)
            restClient = new OfflineClient(this);
        else
            restClient = new SafeClient(this);
    }

    public void setOffline() {
        offlineMode = true;
        initClient();
    }

    public abstract RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password);

    public abstract RestResponse<User> login(String username, String nrn, String password);

    public abstract SessionType getType();

    public abstract User getUser();

    public abstract WalletRegister initWalletRegister();

    public boolean askForPermission(String password) {
        return restClient.login(getUser().getName(), getUser().getId(), password, getType().toString()).isSuccess();
    }

    public String getUserName() {
        return getUser().getName();
    }

    public boolean isOpen() {
        return getUser() != null;
    }

    public void close() {
        bankRegister = null;
        customerRegister = null;
        operationRegister = null;
        offlineMode = false;
        initClient();
    }

    public void alertNoServer() {
        viewsManager.alertNoServer();
    }

    public Optional<Customer> findCustomer(String id) {
        Optional<Customer> optionalCustomer = customerRegister.findCustomer(id);
        if (optionalCustomer.isPresent())
            return optionalCustomer;

        RestResponse<Customer> response = restClient.findCustomer(id);
        if (response.isSuccess()) {
            customerRegister.addCustomer(response.getPayload());
            return Optional.of(response.getPayload());
        }
        return Optional.empty();
    }

    public void addCustomer(Customer customer) {
        customerRegister.addCustomer(customer);
    }

    public RestResponse<Boolean> createAccount(Account account) {
        assert isOpen();
        return restClient.createAccount(account);
    }

    public RestResponse<Boolean> createTransfer(Transfer transfer) {
        assert isOpen();
        return restClient.createTransfer(transfer);
    }

    public WalletRegister getWalletRegister() {
        assert isOpen();
        return restClient.getWalletRegister();
    }

    public BankRegister getBankRegister() {
        if (bankRegister == null)
            bankRegister = restClient.getBankRegister();
        return bankRegister;
    }

    public List<Operation> getOperations(Account account) {
        List<Operation> operations = operationRegister.getOperations(account);
        if (operations != null)
            return operations;

        RestResponse<List<Operation>> response = restClient.getOperations(account);
        if (response.isSuccess()) {
            operationRegister.add(account, response.getPayload());
            return operationRegister.getOperations(account);
        }
        return null;
    }

    public List<Request> getRequests() {
        RestResponse<List<Request>> response = restClient.getRequests();
        if (response.isSuccess())
            return response.getPayload();
        return List.of();
    }

    public CustomerRegister getCustomerRegister() {
        return customerRegister;
    }

    public boolean updateRequest(Request request, Decision decision) {
        return restClient.updateRequest(request, decision).isSuccess();
    }

    public void updatePassword(String newPassword) {
        restClient.changePassword(newPassword);
    }

    public void disableAccount(Account account) {
        restClient.disableAccount(account);
    }

    public void closeAccount(Account account) {
        restClient.closeAccount(account);
    }

    public String getUnusedValidIban() {
        return restClient.getUnusedValidIban();
    }
}
