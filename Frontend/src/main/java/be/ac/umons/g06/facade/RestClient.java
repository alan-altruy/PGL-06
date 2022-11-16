package be.ac.umons.g06.facade;

import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.wallet.WalletRegister;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface used by the active session to interact with the server (or simulate an interaction with the server).
 */
public interface RestClient {

    /**
     * Send a signup request
     * @param username The username of the new customer
     * @param nrn The national registration number of the new customer
     * @param birthdate The birthdate of the new customer
     * @param password The password that will be used by the customer
     * @return A response which will be true if the server has registered the new customer
     */
    RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password);

    /**
     * Send a login request
     * @param username The username of the user
     * @param id The national registration number if the user is a customer, the swift if the user is a bank
     * @param password The password of the user
     * @param type The type of the user ("BANK" or "CUSTOMER")
     * @return An object that is the logged user
     */
    RestResponse<User> login(String username, String id, String password, String type);

    /**
     * Try to find a customer
     * @param id The username or the national registration number of the customer
     * @return A response that contains the customer if one is found, an empty response if no customer with the given id
     * as username or national registration number is found.
     */
    RestResponse<Customer> findCustomer(String id);

    /**
     * Send an account creation request
     * @param account The account that will be created if the request is accepted
     * @return A response which will be true if the server has registered the account creation request
     */

    RestResponse<Boolean> createAccount(Account account);

    /**
     * Send a transfer request
     * @param transfer The transfer that will be registered if the request is accepted
     * @return A response which will be true if the server has registered the transfer request
     */
    RestResponse<Boolean> createTransfer(Transfer transfer);

    /**
     * Update the decision that the logged user has taken concerning a request
     * @param request The concerned request
     * @param decision The new decision
     * @return A response which will be true if the server has registered the update of the request
     */
    RestResponse<Boolean> updateRequest(Request request, Decision decision);

    /**
     * Update the password of the logged user
     * @param newPassword The new password
     * @return A response which will be true if the server has registered the new password
     */
    RestResponse<Boolean> changePassword(String newPassword);

    /**
     * Disable the given account for the logged user
     * @param account The account that will be disabled
     * @return A response which will be true if the server has registered the disabling of the account
     */
    RestResponse<Boolean> disableAccount(Account account);

    /**
     * Send an account closure request
     * @param account The account that will be closed if the request is accepted
     * @return A response which will be true if the server has registered the account closure request
     */
    RestResponse<Boolean> closeAccount(Account account);

    /**
     * Get the list of all the existing banks
     * @return A BankRegister that contains all the existing banks
     */
    BankRegister getBankRegister();

    /**
     * Get the list of the accounts that belongs to the logged user
     * @return A WalletRegister with all the accounts of the logged user
     */
    WalletRegister getWalletRegister();

    /**
     * Get the list of the operations of an account
     * @param account The account
     * @return A response that contains the list of operations of the account
     */
    RestResponse<List<Operation>> getOperations(Account account);

    /**
     * Get the list of the requests that concerns the logged user
     * @return A response that contains the list of requests
     */
    RestResponse<List<Request>> getRequests();

    /**
     * Get an unused valid iban from the server
     * @return A string that is a valid iban at that is not used by any existing account
     */
    String getUnusedValidIban();
}
