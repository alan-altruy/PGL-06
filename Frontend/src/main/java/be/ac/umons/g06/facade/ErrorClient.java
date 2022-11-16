package be.ac.umons.g06.facade;

import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;
import be.ac.umons.g06.model.wallet.CustomerWalletRegister;
import be.ac.umons.g06.model.wallet.WalletRegister;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The client is used when an error occurs while trying to join the server. It only returns placeholder objets in order
 * to avoid NullPointerException.
 */
public class ErrorClient implements RestClient {

    // Constants
    private final Bank NULL_BANK = new Bank("null", "null");

    private static final Customer NULL_CUSTOMER =
            new Customer("", "", LocalDate.of(1,1,1));

    private final Account nullAccount = new AccountBuilder()
            .type(AccountType.CURRENT_ACCOUNT)
            .iban("null")
            .bank(NULL_BANK)
            .ownership(new OwnershipBuilder().type(OwnershipType.INDIVIDUAL).owner(NULL_CUSTOMER).build())
            .creationDate(LocalDate.of(1,1,1))
            .build();

    @Override
    public BankRegister getBankRegister() {
        ArrayList<Bank> banks = new ArrayList<>();
        banks.add(new Bank("null", "null"));
        return new BankRegister(banks);
    }

    @Override
    public WalletRegister getWalletRegister() {
        WalletRegister walletRegister = new CustomerWalletRegister();
        walletRegister.add(nullAccount);
        return walletRegister;
    }

    @Override
    public RestResponse<List<Operation>> getOperations(Account account) {
        return new RestResponse<>(new ArrayList<>());
    }

    @Override
    public RestResponse<List<Request>> getRequests() {
        return new RestResponse<>(new ArrayList<>());
    }

    @Override
    public String getUnusedValidIban() {
        return "BE46548219355936";
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        return new RestResponse<>(Boolean.FALSE, Boolean.FALSE);
    }

    @Override
    public RestResponse<User> login(String username, String id, String password, String type) {
        return type.equals("CUSTOMER") ? new RestResponse<>(NULL_CUSTOMER) : new RestResponse<>(NULL_BANK);
    }

    @Override
    public RestResponse<Customer> findCustomer(String id) {
        return new RestResponse<>(NULL_CUSTOMER);
    }

    @Override
    public RestResponse<Boolean> createAccount(Account account) {
        return new RestResponse<>(Boolean.FALSE, false);
    }

    @Override
    public RestResponse<Boolean> createTransfer(Transfer transfer) {
        return new RestResponse<>(Boolean.FALSE, false);
    }

    @Override
    public RestResponse<Boolean> updateRequest(Request request, Decision decision) {
        return new RestResponse<>(Boolean.FALSE, false);
    }

    @Override
    public RestResponse<Boolean> changePassword(String newPassword) {
        return new RestResponse<>(Boolean.FALSE, false);
    }

    @Override
    public RestResponse<Boolean> disableAccount(Account account) {
        return new RestResponse<>(Boolean.FALSE, false);
    }

    @Override
    public RestResponse<Boolean> closeAccount(Account account) {
        return new RestResponse<>(Boolean.FALSE, false);
    }
}