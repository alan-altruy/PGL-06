package be.ac.umons.g06.session;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.User;
import be.ac.umons.g06.model.wallet.BankWalletRegister;
import be.ac.umons.g06.model.wallet.WalletRegister;

import java.time.LocalDate;

/**
 * The session that is launch when the App destined to a bank employee is used.
 */
public class EmployeeAppSession extends Session {

    private Bank bank;

    public EmployeeAppSession(ViewsManager manager) {
        super(manager);
    }

    @Override
    public User getUser() {
        return bank;
    }

    @Override
    public WalletRegister initWalletRegister() {
        return new BankWalletRegister();
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        throw new IllegalArgumentException("This method cannot be called in this class");
    }

    @Override
    public RestResponse<User> login(String username, String nrn, String password) {
        RestResponse<User> response = restClient.login(username, nrn, password, getType().toString());
        bank = response.getPayload() != null ? (Bank) response.getPayload() : null;
        return new RestResponse<>(bank, response.isSuccess(), response.getMessage());
    }

    @Override
    public SessionType getType() {
        return SessionType.BANK;
    }

    @Override
    public void close() {
        bank = null;
        super.close();
    }
}
