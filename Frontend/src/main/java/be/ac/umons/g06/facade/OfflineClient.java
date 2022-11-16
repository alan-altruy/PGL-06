package be.ac.umons.g06.facade;

import be.ac.umons.g06.model.*;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.EventType;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;
import be.ac.umons.g06.model.wallet.WalletRegister;
import be.ac.umons.g06.session.Session;
import be.ac.umons.g06.session.SessionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Used in the development process, to allow testing the GUI without having to run a server at the same time. This
 * client is used by the session when the session is opened through the "Online mode" button on the first view of the
 * GUI.
 */
public class OfflineClient implements RestClient {

    // Constants

    LocalDate date1 = LocalDate.of(1995, 10, 22);
    LocalDate date2 = LocalDate.of(2007, 12, 31);

    private final Customer luckyLuke = new Customer("Lucky Luke", "F000000LL", date1);
    private final Customer rantanplan = new Customer("Rantanplan", "F000001RR", date2);
    private final Customer largoWinch = new Customer("Largo Winch", "F000002LW", date1);
    private final Customer tintin = new Customer("Tintin", "F000003TT", date2);

    Ownership luckyLukeOwnership = new OwnershipBuilder()
            .type(OwnershipType.INDIVIDUAL)
            .owner(luckyLuke)
            .build();

    private final Bank BELFIUS = new Bank("bel-swift", "Belfius");
    private final Bank ING = new Bank("ing-swift", "ING");
    private final Bank BNP = new Bank("bnp-swift", "BNP");
    private final Bank HSBC = new Bank("hsbc-swift", "HSBC");

    //End of constants declaration

    private final Session session;

    public OfflineClient(Session session) {
        this.session = session;
    }

    @Override
    public RestResponse<Boolean> signup(String username, String nrn, LocalDate birthdate, String password) {
        return new RestResponse<>(Boolean.FALSE, false, "offline_activated");
    }

    @Override
    public RestResponse<User> login(String username, String id, String password, String type) {
        return type.equals("CUSTOMER") ? new RestResponse<>(luckyLuke) : new RestResponse<>(BELFIUS);
    }

    @Override
    public RestResponse<Customer> findCustomer(String id) {
        switch (id) {
            case "Lucky Luke":
            case "F000000LL":
                return new RestResponse<>(luckyLuke);
            case "Rantanplan":
            case "F000001RR":
                return new RestResponse<>(rantanplan);
            case "Largo Winch":
            case "F000002LW":
                return new RestResponse<>(largoWinch);
            case "Tintin":
            case "F000003TT":
                return new RestResponse<>(tintin);
            default:
                return new RestResponse<>(new Customer("John Wick", id, LocalDate.of(2000,1,1)));
        }
    }

    @Override
    public RestResponse<Boolean> createAccount(Account account) {
        return new RestResponse<>(Boolean.TRUE);
    }

    @Override
    public RestResponse<Boolean> createTransfer(Transfer transfer){
        return new RestResponse<>(Boolean.TRUE);
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

    @Override
    public BankRegister getBankRegister() {
        return new BankRegister(List.of(BELFIUS, ING, BNP, HSBC));
    }

    @Override
    public WalletRegister getWalletRegister() {
        WalletRegister walletRegister;
        session.addCustomer(luckyLuke);
        session.addCustomer(largoWinch);
        session.addCustomer(tintin);
        session.addCustomer(rantanplan);
        walletRegister = session.initWalletRegister();
        if (session.getType().equals(SessionType.CUSTOMER))
            walletRegister.add(List.of(
                    getAccount1(), getAccount2(), getAccount3(),
                    getAccount4(), getAccount5(), getAccount6()));
        else
            walletRegister.add(List.of(getAccount1(), getAccount2(), getAccount3(), getAccount4()));
        return walletRegister;
    }

    @Override
    public RestResponse<List<Operation>> getOperations(Account account) {
        return new RestResponse<>(getRandomOperationList(account));
    }

    @Override
    public RestResponse<List<Request>> getRequests() {
        return new RestResponse<>(List.of());
    }

    @Override
    public String getUnusedValidIban() {
        return "BE46548219355936";
    }

    private Account getAccount1() {
        return new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE80451244956224")
                .ownership(luckyLukeOwnership)
                .bank(BELFIUS)
                .creationDate(LocalDate.of(2011, 11, 6))
                .balance(3565)
                .build();
    }

    private Account getAccount2() {
        return new AccountBuilder()
                .type(AccountType.TERM_ACCOUNT)
                .iban("BE80451244956225")
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.INDIVIS)
                        .owner(luckyLuke)
                        .owner(largoWinch)
                        .owner(tintin)
                        .build())
                .bank(BELFIUS)
                .creationDate(LocalDate.of(2016, 5, 8))
                .balance(45542)
                .build();
    }

    private Account getAccount3() {
        return new AccountBuilder()
                .type(AccountType.SAVINGS_ACCOUNT)
                .iban("BE80444742112254")
                .bank(BELFIUS)
                .ownership(luckyLukeOwnership)
                .linkedAccountIban("BE80451244956224")
                .creationDate(LocalDate.of(2012, 6, 27))
                .balance(1220004)
                .build();
    }

    private Account getAccount4() {
        return new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE80444481122546")
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.YOUNG)
                        .owner(rantanplan)
                        .supervisor(luckyLuke)
                        .build())
                .bank(BELFIUS)
                .creationDate(LocalDate.of(2012, 6, 12))
                .balance(5001)
                .build();
    }

    private Account getAccount5() {
        return new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE70451244956224")
                .ownership(luckyLukeOwnership)
                .bank(ING)
                .creationDate(LocalDate.of(2011, 11, 6))
                .balance(35065)
                .build();
    }

    private Account getAccount6() {
        return new AccountBuilder()
                .type(AccountType.SAVINGS_ACCOUNT)
                .iban("BE70444742112254")
                .bank(ING)
                .ownership(luckyLukeOwnership)
                .linkedAccountIban("BE80451244956224")
                .creationDate(LocalDate.of(2010, 6, 27))
                .balance(12401)
                .build();
    }

    public static int randInt(float min, float max) {
        Random rand = new Random();
        float f = rand.nextFloat() * (max - min) + min;
        return Math.round(f);
    }

    private static List<Operation> getRandomOperationList(Account account) {
        List<Operation> list = new ArrayList<>();
        LocalDate currentDate = account.getCreationDate();
        LocalDate endDate = account.getClosingDate() == null ? LocalDate.now() : account.getClosingDate();

        list.add(new Operation(1L, EventType.CREATION, 0,
                currentDate.atStartOfDay(), account.getIban(), "account_creation"));

        while(currentDate.compareTo(endDate) < 0) {
            int amount = Math.random() < 0.5 ? randInt(0, 100000) : randInt(-100000, 0);
            list.add(new Operation(
                    1L,
                    EventType.TRANSFER,
                    amount,
                    currentDate.atStartOfDay(),
                    account.getIban(),
                    amount > 0 ? "{transfer} {from} BE4521454" : "{transfer} {to} BE45288745"));
            currentDate = currentDate.plusDays(randInt(1, 7));
        }
        return list;
    }
}
