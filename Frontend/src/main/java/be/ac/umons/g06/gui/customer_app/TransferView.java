package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.Transfer;
import be.ac.umons.g06.model.wallet.WalletRegister;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.TransferBuilder;

import java.net.URL;
import java.util.*;

public class TransferView extends InnerView implements Initializable {

    @FXML
    private BorderPane transferPane;

    @FXML
    private Button confirmButton;

    @FXML
    private ComboBox<String> bankComboBox;

    @FXML
    private ComboBox<String> accountComboBox;

    @FXML
    private HBox transferTypeHBox;

    @FXML
    private VBox destinationAccountVBox;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextArea communicationTextArea;

    @FXML
    private TextField structuredCommunicationTextField;

    @FXML
    private Label fromErrorLabel;

    @FXML
    private Label toErrorLabel;

    private ComboBox<String> internAccountComboBox;
    private TextField ibanTextField;
    private boolean isExtern;

    @Override
    public Pane getView() {
        return transferPane;
    }

    @Override
    protected String getTitle() {
        return I18N.get("new_transfer");
    }

    @Override
    public String getFXMLPath() {
        return "fxml/transfer_view.fxml";
    }

    private void updateDestinationAccount(boolean isExtern) {
        this.isExtern = isExtern;
        internAccountComboBox.setDisable(isExtern);
        ibanTextField.setDisable(!isExtern);
        if (isExtern) {
            destinationAccountVBox.getChildren().remove(internAccountComboBox);
            destinationAccountVBox.getChildren().add(ibanTextField);
        } else {
            destinationAccountVBox.getChildren().remove(ibanTextField);
            destinationAccountVBox.getChildren().add(internAccountComboBox);
        }
    }

    private String reviseAmount(String addedText) {
        addedText = addedText.replaceAll("\\.", ",");
        String amountText = amountTextField.getText() + addedText;
        return amountText.matches("[0-9]{1,14},?[0-9]{0,2}") ? addedText : "";
    }

    private boolean checkAmount(){
        String amountTxt = amountTextField.getText();
        return amountTxt.matches("[0-9]{1,14},[0-9]{2}");
    }

    private String reviseFreeCommunication(String freeCommunication) {
        return communicationTextArea.getText().length() < 105 ? freeCommunication : "";
    }

    private String reviseStructuredCommunication(String structComm) {
        return structComm.matches("(\\d|/)*") ? structComm : "";
    }

    private boolean checkStructuredCommunication() {
        String structuredCommunicationText = structuredCommunicationTextField.getText();
        if (structuredCommunicationText.length()==0)
                return true;
        if (structuredCommunicationText.matches("(\\d{3}/\\d{4}/\\d{5})|\\d{12}")) {
            long structuredCommunication = Long.parseLong(structuredCommunicationText.replaceAll("/", ""));
            long firstDigits = structuredCommunication / 100;
            long mod97 = (firstDigits % 97 == 0) ? 97 : firstDigits % 97;
            return mod97 == structuredCommunication % 100;
        }
        return false;
    }

    private Transfer buildTransfer(){
        return new TransferBuilder()
                .originIban(Util.getAccountIbanFromDescription(accountComboBox.getValue()))
                .destinationIban(isExtern ? ibanTextField.getText() : Util.getAccountIbanFromDescription(internAccountComboBox.getValue()))
                .amount(Integer.parseInt(amountTextField.getText().replaceAll(",", "")))
                .communication(communicationTextArea.getText())
                .structuredCommunication(structuredCommunicationTextField.getText())
                .build();
    }

    private void displayConfirmationDialog() {
        PasswordAlert passwordAlert = new PasswordAlert();
        if (passwordAlert.test())
            sendRequest();
    }

    private void sendRequest() {
        RestResponse<Boolean> requestResponse = manager.getSession().createTransfer(buildTransfer());
        if (requestResponse.isSuccess())
            displaySuccessAlert();
    }

    private void displaySuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(I18N.get("success"));
        successAlert.setContentText(I18N.get("request_sent"));
        successAlert.setOnCloseRequest(event -> manager.setPreviousView());
        successAlert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        internAccountComboBox = new ComboBox<>();
        ibanTextField = new TextField();
        ibanTextField.setPromptText("BE61 3101 2698 5517");
        WalletRegister walletRegister = manager.getSession().getWalletRegister();

        bankComboBox.getItems().setAll(walletRegister.getAllBanksNames());
        bankComboBox.setOnAction(event -> {
            Collection<String> accountsNames = Util
                    .getAccountsDescriptions(walletRegister.getWalletByBankName(bankComboBox.getValue()).getAccounts());
            accountComboBox.getItems().setAll(accountsNames);
            accountComboBox.getSelectionModel().selectFirst();
            accountsNames.remove(accountComboBox.getSelectionModel().getSelectedItem());
            internAccountComboBox.getItems().setAll(accountsNames);
            internAccountComboBox.getSelectionModel().selectFirst();
        });
        bankComboBox.getSelectionModel().selectFirst();
        bankComboBox.fireEvent(new ActionEvent());

        accountComboBox.setOnAction(event -> {
            Collection<String> accountsNames = Util
                    .getAccountsDescriptions(walletRegister.getWalletByBankName(bankComboBox.getValue()).getAccounts());
            accountsNames.remove(accountComboBox.getSelectionModel().getSelectedItem());
            internAccountComboBox.getItems().setAll(accountsNames);
            internAccountComboBox.getSelectionModel().selectFirst();
        });

        amountTextField.setPromptText("0.00" + Util.EURO);
        amountTextField.setTextFormatter(new TextFormatter<>(filter -> {
            filter.setText(reviseAmount(filter.getText()));
            return filter;
        }));

        ToggleGroup transferTypeGroup = new ToggleGroup();
        RadioButton internalRadioButton = new RadioButton(I18N.get("internal"));
        transferTypeHBox.getChildren().add(internalRadioButton);
        internalRadioButton.setToggleGroup(transferTypeGroup);
        internalRadioButton.setOnAction(event -> updateDestinationAccount(false));
        RadioButton externalRadioButton = new RadioButton(I18N.get("external"));
        transferTypeHBox.getChildren().add(externalRadioButton);
        externalRadioButton.setToggleGroup(transferTypeGroup);
        externalRadioButton.setOnAction(event -> updateDestinationAccount(true));
        internalRadioButton.fire();

        ibanTextField.setTextFormatter(new TextFormatter<>(filter -> {
            filter.setText(Util.reviseIban(ibanTextField.getText(), filter.getText()));
            return filter;
        }));

        communicationTextArea.setTextFormatter(new TextFormatter<>(filter -> {
            filter.setText(reviseFreeCommunication(filter.getText()));
            return filter;
        }));
        communicationTextArea.setWrapText(true);

        structuredCommunicationTextField.setTextFormatter(new TextFormatter<>(filter -> {
            filter.setText(reviseStructuredCommunication(filter.getText()));
            return filter;
        }));

        confirmButton.setOnAction(event -> {
            fromErrorLabel.setText("");
            toErrorLabel.setText("");
            if (!checkAmount())
                fromErrorLabel.setText(I18N.get("invalid_amount"));
            else if (externalRadioButton.isSelected() && !Account.isValidIban(ibanTextField.getText()))
                toErrorLabel.setText(I18N.get("invalid_iban"));
            else if (!checkStructuredCommunication())
                toErrorLabel.setText(I18N.get("invalid_structured_communication"));
            else if (internalRadioButton.isSelected())
                sendRequest();
            else
                displayConfirmationDialog();
        });
    }

    private static class PasswordAlert extends Alert {

        private final PasswordField passwordField;

        public PasswordAlert() {
            super(AlertType.CONFIRMATION);
            setTitle(I18N.get("confirm_transfer"));
            VBox alertVBox = new VBox();
            alertVBox.getChildren().add(new Label(I18N.get("password")));
            passwordField = new PasswordField();
            alertVBox.getChildren().add(passwordField);
            getDialogPane().setContent(alertVBox);
        }

        public boolean test() {
            Optional<ButtonType> result = showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                if (ViewsManager.getInstance().getSession().askForPermission(passwordField.getText()))
                    return true;
                else
                    return test();
            }
            return false;
        }
    }
}