package be.ac.umons.g06.gui.common.accountCreationView;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import be.ac.umons.g06.session.SessionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.wallet.Wallet;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipType;
import be.ac.umons.g06.session.SessionType;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountCreationView extends InnerView implements Initializable {

    @FXML
    private BorderPane accountCreationPane;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private HBox accountTypeHBox;

    @FXML
    private TextField ibanTextField;

    @FXML
    private Button generateIbanButton;

    @FXML
    private Label comboBoxLabel;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private VBox linkedAccountVBox;

    @FXML
    private HBox ownershipTypeHBox;

    @FXML
    private VBox ownershipVBox;

    @FXML
    private VBox titleParametersVBox;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label errorLabel;


    private Label linkedAccountLabel;
    private ComboBox<String> linkedAccountComboBox;
    private OwnershipVBoxManager ownershipVBoxManager;
    private AccountType accountType;

    private void updateLinkedAccountVBox(boolean enable) {
        linkedAccountLabel.setDisable(!enable);
        linkedAccountComboBox.setDisable(!enable);
        if (enable) {
            linkedAccountVBox.getChildren().add(linkedAccountLabel);
            linkedAccountVBox.getChildren().add(linkedAccountComboBox);
        }
        else {
            linkedAccountVBox.getChildren().clear();
        }
    }

    private void sendAccountCreationRequest() {
        errorLabel.setText("");
        if (retrieveIban().equals("")) {
            errorLabel.setText(I18N.get("invalid_iban"));
            return;
        }

        Ownership ownership = ownershipVBoxManager.buildOwnership();
        if (ownership == null)
            return;

        Account account = null;
        try {
            AccountBuilder builder = new AccountBuilder()
                    .type(accountType)
                    .bank(retrieveSelectedBank())
                    .iban(retrieveIban())
                    .ownership(ownership);
            if (accountType.equals(AccountType.SAVINGS_ACCOUNT))
                builder = builder.linkedAccountIban(Util.getAccountIbanFromDescription(linkedAccountComboBox.getValue()));
            account = builder.build();

        } catch (NullPointerException e) {
            errorLabel.setText("Error TO DO");
        }
        if (account != null) {
            RestResponse<Boolean> response = manager.getSession().createAccount(account);
            if (response.isSuccess())
                accountCreated();
            else
                errorLabel.setText("Error account cannot be created : " + response.getMessage());
        }
    }

    private void accountCreated() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(I18N.get("success"));
        successAlert.setContentText(I18N.get("account_created"));
        successAlert.setOnCloseRequest(event -> manager.setPreviousView());
        successAlert.showAndWait();
    }

    private Bank retrieveSelectedBank() {
        if (manager.getSession().getType().equals(SessionType.BANK))
            return manager.getSession().getBankRegister().getBankByName(manager.getSession().getUser().getName()).get();
        return manager.getSession().getBankRegister().getBankByName(comboBox.getValue()).get();
    }

    private void setAccountType(AccountType type) {
        accountType = type;
        updateLinkedAccountVBox(type.equals(AccountType.SAVINGS_ACCOUNT));
    }

    private String retrieveIban() {
        return Account.isValidIban(ibanTextField.getText()) ? ibanTextField.getText().replace(" ", "") : "";
    }

    private void updateLinkedAccountComboBox(Wallet wallet) {
        linkedAccountComboBox.getItems().setAll(Util.getAccountsDescriptions(wallet.getAccountsWithType(AccountType.CURRENT_ACCOUNT)));
        linkedAccountComboBox.getSelectionModel().selectFirst();
    }

    @Override
    protected Pane getView() {
        return accountCreationPane;
    }

    @Override
    protected String getTitle() {
        return I18N.get("account_creation");
    }

    @Override
    public String getFXMLPath() {
        return "fxml/account_creation_view.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setPannable(true);

        if (manager.getSession().getType().equals(SessionType.CUSTOMER)) {
            comboBoxLabel.setText(I18N.get("bank"));
            comboBox.getItems().setAll(manager.getSession().getWalletRegister().getAllBanksNames());
            comboBox.getSelectionModel().selectFirst();
            comboBox.setOnAction(event ->
                    updateLinkedAccountComboBox(
                            manager.getSession().getWalletRegister().getWalletByBankName(comboBox.getValue())));
        } else {
            comboBoxLabel.setText(I18N.get("customer"));
            comboBox.getItems().setAll(manager.getSession().getWalletRegister().getAllCustomerNames());
            comboBox.getSelectionModel().selectFirst();
            comboBox.setOnAction(event -> {
                Customer customer = manager.getSession().findCustomer(comboBox.getValue()).get();
                updateLinkedAccountComboBox(
                        manager.getSession().getWalletRegister().getWalletByCustomer(customer));
                ownershipVBoxManager.setMainCustomer(customer);
            });
        }

        ibanTextField.setTextFormatter(new TextFormatter<>(filter -> {
            filter.setText(Util.reviseIban(ibanTextField.getText(), filter.getText()));
            return filter;
        }));
        HBox.setHgrow(ibanTextField, Priority.ALWAYS);
        generateIbanButton.setOnAction(event -> {
            ibanTextField.setText("");
            ibanTextField.setText(manager.getSession().getUnusedValidIban());
        });

        linkedAccountLabel = new Label(I18N.get("linked_current_account"));
        linkedAccountLabel.setUnderline(true);
        linkedAccountComboBox = new ComboBox<>();

        ToggleGroup accountTypeGroup = new ToggleGroup();
        for (AccountType type : AccountType.values()) {
            RadioButton btn = new RadioButton(I18N.get(type.toString()));
            accountTypeHBox.getChildren().add(btn);
            btn.setToggleGroup(accountTypeGroup);
            btn.setOnAction(event -> setAccountType(type));
        }
        ((RadioButton) accountTypeHBox.getChildren().get(0)).fire();

        ownershipVBoxManager = new OwnershipVBoxManager(ownershipVBox, errorLabel);
        if (manager.getSession().getType().equals(SessionType.CUSTOMER))
            ownershipVBoxManager.setMainCustomer((Customer) manager.getSession().getUser());

        ToggleGroup ownershipTypeGroup = new ToggleGroup();
        for (OwnershipType type : OwnershipType.values()) {
            RadioButton btn = new RadioButton(I18N.get(type.toString()));
            ownershipTypeHBox.getChildren().add(btn);
            btn.setToggleGroup(ownershipTypeGroup);
            btn.setOnAction(event -> ownershipVBoxManager.setOwnershipType(type));
        }
        comboBox.fireEvent(new ActionEvent());
        ((RadioButton) ownershipTypeHBox.getChildren().get(0)).fire();

        createAccountButton.setOnAction(event -> sendAccountCreationRequest());
    }
}
