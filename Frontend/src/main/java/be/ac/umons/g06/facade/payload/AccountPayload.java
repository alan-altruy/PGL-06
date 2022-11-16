package be.ac.umons.g06.facade.payload;

import be.ac.umons.g06.model.account.Account;

/**
 * The POJO that matches the json form of account in the body of the response to a successful "GET {base-url}/api/account".
 */
public class AccountPayload {
    private boolean disabled;
    private Account account;

    public Account asAccount() {
        account.setDisabled(disabled);
        return account;
    }
}
