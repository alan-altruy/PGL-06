package be.ac.umons.g06.facade;

import be.ac.umons.g06.facade.payload.AccountPayload;
import be.ac.umons.g06.facade.payload.MessagePayload;
import kong.unirest.*;
import kong.unirest.json.JSONObject;
import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.facade.payload.LoginResponsePayload;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.model.ownership.OwnershipInvolvement;
import be.ac.umons.g06.model.wallet.WalletRegister;
import be.ac.umons.g06.session.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The class that effectively send HTTP request and receives HTTP responses from the server
 */
public class OnlineClient implements RestClient {

    private final String REST_URI = "http://localhost:9000/api/";
    private final String CONTENT_TYPE = "application/json";

    private final Session session;

    private String jwt;

    public OnlineClient(Session session) {
        this.session = session;
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", username);
        requestBody.put("id", nrn);
        requestBody.put("password", password);
        requestBody.put("date", birthdate.toString());

        HttpResponse<String> response = Unirest.post(REST_URI+"auth/signup")
                .body(requestBody)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<User> login(String username, String id, String password, String type) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("name", username);
        requestBody.put("password", password);
        requestBody.put("type", type);

        HttpResponse<LoginResponsePayload> response = Unirest.post(REST_URI+"auth/login")
                .body(requestBody)
                .contentType(CONTENT_TYPE)
                .asObject(LoginResponsePayload.class);

        if (response.isSuccess()) {
            jwt = "Bearer " + response.getBody().getToken();
            return new RestResponse<>(response.getBody().getUser());
        } else if (response.getStatus() == 401) {
            return new RestResponse<>(null, false, "authentication_failed");
        }
        return new RestResponse<>(null, false, "Error " + response.getStatus());
    }

    @Override
    public RestResponse<Customer> findCustomer(String id) {
        HttpResponse<Customer> response = Unirest.get(REST_URI + "customer/{id}")
                .header("Authorization", jwt)
                .routeParam("id", id)
                .asObject(Customer.class);

        String PARSING_ERROR = "parsing_error";
        if (response.getParsingError().isPresent())
            return new RestResponse<>(null, false, PARSING_ERROR);
        String NOT_FOUND = "not_found";
        if (!response.isSuccess())
            return new RestResponse<>(null, false, NOT_FOUND);
        return new RestResponse<>(response.getBody());
    }

    @Override
    public RestResponse<Boolean> createAccount(Account account) {
        HttpResponse<String> response = Unirest.post(REST_URI + "account")
                .header("Authorization", jwt)
                .body(account)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<Boolean> createTransfer(Transfer transfer) {
        HttpResponse<String> response = Unirest.post(REST_URI + "request/transfer")
                .header("Authorization", jwt)
                .body(transfer)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<Boolean> updateRequest(Request request, Decision decision) {
        HttpResponse<String> response = Unirest.post(REST_URI + "request/{id}/{decision}")
                .routeParam("id", String.valueOf(request.getId()))
                .routeParam("decision", decision.toString())
                .header("Authorization", jwt)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<Boolean> changePassword(String newPassword) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("password", newPassword);

        HttpResponse<String> response = Unirest.post(REST_URI+"auth/changePassword")
                .header("Authorization", jwt)
                .body(requestBody)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<Boolean> disableAccount(Account account) {
        HttpResponse<String> response = Unirest.post(REST_URI + "account/{iban}/disable")
                .routeParam("iban", String.valueOf(account.getIban()))
                .header("Authorization", jwt)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public RestResponse<Boolean> closeAccount(Account account) {
        HttpResponse<String> response = Unirest.post(REST_URI + "account/{iban}/close")
                .routeParam("iban", String.valueOf(account.getIban()))
                .header("Authorization", jwt)
                .contentType(CONTENT_TYPE)
                .asString();
        return new RestResponse<>(response.isSuccess(), response.isSuccess(), response.getBody());
    }

    @Override
    public BankRegister getBankRegister() {
        List<Bank> banks = Unirest.get(REST_URI+"banks")
                .header("Authorization", jwt)
                .asObject(new GenericType<List<Bank>>(){})
                .getBody();
        return new BankRegister(banks);
    }

    @Override
    public WalletRegister getWalletRegister() {
        HttpResponse<List<AccountPayload>> response = Unirest.get(REST_URI+"accounts")
                .header("Authorization", jwt)
                .asObject(new GenericType<>(){});

        WalletRegister register = session.initWalletRegister();
        try {
            for (AccountPayload accountPayload : response.getBody()) {
                Account account = accountPayload.asAccount();
                for (OwnershipInvolvement involvement : account.getOwnership().getInvolvements())
                    session.addCustomer(involvement.getCustomer());
                register.add(accountPayload.asAccount());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return register;
    }

    @Override
    public RestResponse<List<Operation>> getOperations(Account account) {
        HttpResponse<List<Operation>> response = Unirest.get(REST_URI + "account/" + account.getIban() + "/operations?dateTime={dateTime}")
                .routeParam("dateTime", LocalDateTime.now().minusYears(50).toString())
                .header("Authorization", jwt)
                .asObject(new GenericType<>() {
                });
        if (response.isSuccess() && response.getParsingError().isEmpty())
            return new RestResponse<>(response.getBody());
        return new RestResponse<>(null, false);
    }

    @Override
    public RestResponse<List<Request>> getRequests() {
        HttpResponse<List<Request>> response = Unirest.get(REST_URI + "request/all?lastUpdate={dateTime}")
                .routeParam("dateTime", LocalDateTime.now().minusYears(50).toString())
                .header("Authorization", jwt)
                .asObject(new GenericType<>() {});

        if (response.isSuccess() && response.getParsingError().isEmpty())
            return new RestResponse<>(response.getBody());
        return new RestResponse<>(null, false);
    }

    @Override
    public String getUnusedValidIban() {
        HttpResponse<MessagePayload> response = Unirest.get(REST_URI + "account/generateIban")
                .header("Authorization", jwt)
                .asObject(MessagePayload.class);
        return response.getBody().getMessage();
    }
}
