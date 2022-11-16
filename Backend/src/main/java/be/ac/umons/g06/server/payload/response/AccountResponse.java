package be.ac.umons.g06.server.payload.response;

import be.ac.umons.g06.server.model.account.Account;

public class AccountResponse {

    /**
     * Account
     */
    private final Account account;
    /**
     * Status of the Account
     */
    private final boolean disabled;

    /**
     * Constructor of AccountResponse
     * @param account The account
     * @param disabled The status of the account
     */
    public AccountResponse(Account account, boolean disabled) {
        this.account = account;
        this.disabled = disabled;
    }

    /**
     * Checks if the account is disabled
     * @return A boolean Object <li>True if the account is disabled</li>
     * <li>False if the account is enabled</li>
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Get the account
     * @return The account
     */
    public Account getAccount() {
        return account;
    }
}
