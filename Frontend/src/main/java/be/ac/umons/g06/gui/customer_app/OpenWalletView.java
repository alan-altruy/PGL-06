package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OpenWalletView extends InnerView implements Initializable {

    @FXML
    private BorderPane openWalletPane;

    @FXML
    private ComboBox<String> bankComboBox;

    @FXML
    private Button requestButton;

    private Bank retrieveSelectedBank() {
        return manager.getSession().getBankRegister().getBankByName(bankComboBox.getValue()).get();
    }

    private void requestSent() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(I18N.get("success"));
        successAlert.setContentText(I18N.get("request_sent"));
        successAlert.setOnCloseRequest(event -> {
            manager.setPreviousView();
        });
        successAlert.showAndWait();
    }

    private void sendWalletCreationRequest(){
        Ownership ownership = new OwnershipBuilder()
                .type(OwnershipType.INDIVIDUAL)
                .owner((Customer) manager.getSession().getUser())
                .build();
        Account account = new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .bank(retrieveSelectedBank())
                .iban(manager.getSession().getUnusedValidIban())
                .ownership(ownership)
                .build();

        RestResponse<Boolean> response = manager.getSession().createAccount(account);
        if (response.isSuccess())
            requestSent();
    }

    @Override
    protected Pane getView() {
        return openWalletPane;
    }

    @Override
    public String getFXMLPath() {
        return "fxml/open_wallet_view.fxml";
    }

    @Override
    protected String getTitle() {
        return I18N.get("open_wallet");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bankComboBox.getItems().setAll(manager.getSession().getBankRegister().getAllBanksNames());
        Collection<Bank> allBanks = manager.getSession().getBankRegister().getAllBanks();
        for (Bank bank : manager.getSession().getWalletRegister().getAllBanks())
            allBanks.remove(bank);
        bankComboBox.getItems().setAll(allBanks.stream().map(Bank::getName).collect(Collectors.toList()));
        bankComboBox.getSelectionModel().selectFirst();

        requestButton.setOnAction(event -> sendWalletCreationRequest());
    }
}