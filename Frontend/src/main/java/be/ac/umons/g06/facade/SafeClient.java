package be.ac.umons.g06.facade;

import kong.unirest.UnirestException;
import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.model.wallet.WalletRegister;
import be.ac.umons.g06.session.Session;

import java.time.LocalDate;
import java.util.List;

/**
 * The client used by the session to reach the server in online mode. This client tries to join the server through
 * an OnlineClient and if an error occurs it 1) calls the alertNoServer method of the session and 2) returns a
 * placeholder object from an ErrorClient.
 */
public class SafeClient implements RestClient {

    private final Session session;
    private final OnlineClient onlineClient;
    private final ErrorClient errorClient;

    public SafeClient(Session session) {
        this.session = session;
        onlineClient = new OnlineClient(session);
        errorClient = new ErrorClient();
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        try {
            return onlineClient.signup(username, nrn, birthdate, password);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.signup(username, nrn, birthdate, password);
        }
    }

    @Override
    public RestResponse<User> login(String username, String id, String password, String type) {
        try {
            return onlineClient.login(username, id, password, type);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.login(username, id, password, type);
        }
    }

    @Override
    public RestResponse<Customer> findCustomer(String id) {
        try {
            return onlineClient.findCustomer(id);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.findCustomer(id);
        }
    }

    @Override
    public RestResponse<Boolean> createAccount(Account account) {
        try {
            return onlineClient.createAccount(account);
        } catch (UnirestException e) {
            System.out.println(e.getMessage());
            session.alertNoServer();
            return errorClient.createAccount(account);
        }
    }

    @Override
    public RestResponse<Boolean> createTransfer(Transfer transfer) {
        try {
            return onlineClient.createTransfer(transfer);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.createTransfer(transfer);
        }
    }

    @Override
    public RestResponse<Boolean> updateRequest(Request request, Decision decision) {
        try {
            return onlineClient.updateRequest(request, decision);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.updateRequest(request, decision);
        }
    }

    @Override
    public RestResponse<Boolean> changePassword(String newPassword) {
        try {
            return onlineClient.changePassword(newPassword);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.changePassword(newPassword);
        }
    }

    @Override
    public RestResponse<Boolean> disableAccount(Account account) {
        try {
            return onlineClient.disableAccount(account);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.disableAccount(account);
        }
    }

    @Override
    public RestResponse<Boolean> closeAccount(Account account) {
        try {
            return onlineClient.closeAccount(account);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.closeAccount(account);
        }
    }

    @Override
    public BankRegister getBankRegister() {
        try {
            return onlineClient.getBankRegister();
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.getBankRegister();
        }
    }

    @Override
    public WalletRegister getWalletRegister() {
        try {
            return onlineClient.getWalletRegister();
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.getWalletRegister();
        }
    }

    @Override
    public RestResponse<List<Operation>> getOperations(Account account) {
        try {
            return onlineClient.getOperations(account);
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.getOperations(account);
        }
    }

    @Override
    public RestResponse<List<Request>> getRequests() {
        try {
            return onlineClient.getRequests();
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.getRequests();
        }
    }

    @Override
    public String getUnusedValidIban() {
        try {
            return onlineClient.getUnusedValidIban();
        } catch (UnirestException e) {
            session.alertNoServer();
            return errorClient.getUnusedValidIban();
        }
    }
}
