package be.ac.umons.g06.gui.common.util;

import be.ac.umons.g06.gui.common.ViewsManager;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.session.Session;

import java.util.*;

/**
 * This object gives access to two javafx components : 1) a ComboBox that allows to select an account or a set of
 * accounts and 2) a FlowPane that shows all the selected items as buttons. One item can be unselected by clicking on
 * the associated button.
 */
public class AccountSelector {

    private final Session session;

    /**
     * The FlowPane that contains all the buttons that allows to see the list of selected accounts and delete on of them
     * by clicking on the associated button
     */
    private final FlowPane flowPane;

    /**
     * The comboBox that allows to add an account or a set of accounts to the selected account list
     */
    private final ComboBox<String> comboBox;

    /**
     * Adding a selectable value in the comboBox triggers the listener on the comboBox and add the value in the
     * selected values. To avoid this behaviour, this boolean allows to lock the comboBox to not update the selected
     * account list while we add new values in the bankComboBox;
     */
    private boolean comboBoxLocked;
    private final List<Item> comboBoxList;
    private final List<Item> selectedList;

    public AccountSelector() {
        super();
        session = ViewsManager.getInstance().getSession();

        comboBox = new ComboBox<>();
        flowPane = new FlowPane();
        flowPane.setOrientation(Orientation.HORIZONTAL);
        flowPane.setHgap(2);
        flowPane.setVgap(2);
        flowPane.setPrefWrapLength(600);
        comboBoxList = new ArrayList<>();
        selectedList = new LinkedList<>();

        comboBox.setOnAction(event -> {
            if (!comboBoxLocked)
                updateSelectedAccounts();
        });
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public FlowPane getSelectedAccountsPane() {
        return flowPane;
    }

    private void updateSelectedAccounts() {
        if (!selectedList.contains(getComboBoxValue())) {
            addBtn(getComboBoxValue());
            selectedList.add(getComboBoxValue());
        }
    }

    private Item getComboBoxValue() {
        return comboBoxList.get(comboBox.getSelectionModel().getSelectedIndex());
    }

    private void addBtn(Item item) {
        Button btn = new Button(item.btnText);
        btn.setOnAction(btnEvent -> {
            selectedList.remove(item);
            flowPane.getChildren().remove(btn);
        });
        flowPane.getChildren().add(btn);
    }

    public void setBank(Bank bank) {
        updateComboBox(false, bank, null);
    }

    public void setCustomer(Customer customer) {
        updateComboBox(false, null, customer);
    }

    public void setAll() {
        updateComboBox(true, null, null);
    }

    private void addComboBoxItem(Item item) {
        comboBoxList.add(item);
        comboBox.getItems().add(item.comboBoxText);
    }

    private void updateComboBox(boolean all, Bank bank, Customer customer) {
        comboBoxLocked = true;
        comboBoxList.clear();
        comboBox.getItems().clear();

        if (all)
            updateComboBoxAll();
        else if (bank == null)
            updateComboBox(customer);
        else
            updateComboBox(bank);

        comboBoxLocked = false;
        comboBox.getSelectionModel().selectFirst();
        comboBox.fireEvent(new ActionEvent());
    }

    private void updateComboBoxAll() {
        addComboBoxItem(new Item(I18N.get("all_accounts"), session.getWalletRegister().getAllAccounts()));
        for (AccountType type : AccountType.values())
            addComboBoxItem(new Item(
                    I18N.get(type.toString()) + " | " + I18N.get("account_selector.all"),
                    session.getWalletRegister().getAllAccountsWithType(type)));
        for (Account account : session.getWalletRegister().getAllAccounts())
            addComboBoxItem(new Item(
                    Util.getAccountDescription(account),
                    account.prettyPrintIban(),
                    account));
    }

    private void updateComboBox(Bank bank) {
        addComboBoxItem(new Item(bank.getName() + " | " + I18N.get("all_accounts"), session.getWalletRegister().getWalletByBank(bank).getAccounts()));
        for (AccountType type : AccountType.values())
            addComboBoxItem(new Item(
                    bank.getName() + " | " + I18N.get(type.toString()) + " | " + I18N.get("account_selector.all"),
                    session.getWalletRegister().getWalletByBank(bank).getAccountsWithType(type)));
        for (Account account : session.getWalletRegister().getWalletByBank(bank).getAccounts())
            addComboBoxItem(new Item(
                    Util.getAccountDescription(account),
                    account.prettyPrintIban(),
                    account));
    }

    private void updateComboBox(Customer customer) {
        addComboBoxItem(new Item(customer.getName() + " | " + I18N.get("all_accounts"), session.getWalletRegister().getWalletByCustomer(customer).getAccounts()));
        for (AccountType type : AccountType.values())
            addComboBoxItem(new Item(
                    customer.getName() + " | " + I18N.get(type.toString()) + " | " + I18N.get("account_selector.all"),
                    session.getWalletRegister().getWalletByCustomer(customer).getAccountsWithType(type)));
        for (Account account : session.getWalletRegister().getWalletByCustomer(customer).getAccounts())
            addComboBoxItem(new Item(
                    Util.getAccountDescription(account),
                    account.prettyPrintIban(),
                    account));
    }

    public Set<Account> getSelectedAccounts() {
        Set<Account> accounts = new HashSet<>();
        for (Item item : selectedList)
            accounts.addAll(item.accounts);
        return accounts;
    }

    private static class Item {
        private final String comboBoxText;
        private final String btnText;
        private final Set<Account> accounts;

        private Item(String text, Collection<Account> accounts) {
            this(text, text, accounts);
        }

        private Item(String comboBoxText, String btnText, Collection<Account> accounts) {
            this.comboBoxText = comboBoxText;
            this.btnText = btnText;
            this.accounts = new HashSet<>(accounts);
        }

        private Item(String comboBoxText, String btnText, Account account) {
            this.comboBoxText = comboBoxText;
            this.btnText = btnText;
            this.accounts = new HashSet<>();
            accounts.add(account);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Item other = (Item) o;
            return btnText.equals(other.btnText);
        }
    }
}
