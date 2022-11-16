package be.ac.umons.g06.session;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.User;
import be.ac.umons.g06.model.wallet.CustomerWalletRegister;
import be.ac.umons.g06.model.wallet.WalletRegister;

import java.time.LocalDate;

/**
 * The session that is launch when the App destined to lambda customer is used.
 */
public class CustomerAppSession extends Session {

    private Customer customer;

    public CustomerAppSession(ViewsManager manager) {
        super(manager);
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        RestResponse<Boolean> response = restClient.signup(username, nrn, birthdate, password);
        if (response.isSuccess())
            login(username, nrn, password);
        return response;
    }

    @Override
    public User getUser() {
        return customer;
    }

    @Override
    public WalletRegister initWalletRegister() {
        return new CustomerWalletRegister();
    }

    public RestResponse<User> login(String username, String nrn, String password) {
        RestResponse<User> response = restClient.login(username, nrn, password, getType().toString());
        customer = response.getPayload() != null ? (Customer) response.getPayload() : null;
        return new RestResponse<>(customer, response.isSuccess(), response.getMessage());
    }

    @Override
    public SessionType getType() {
        return SessionType.CUSTOMER;
    }

    @Override
    public void close() {
        customer = null;
        super.close();
    }
}
