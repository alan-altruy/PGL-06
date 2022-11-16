package be.ac.umons.g06.gui.employee_app;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import be.ac.umons.g06.model.Bank;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountBuilder;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;
import be.ac.umons.g06.model.wallet.Wallet;
import be.ac.umons.g06.session.Session;

import java.util.Optional;

public class AddCustomerView extends InnerView {

    private final BorderPane mainPane;
    private final Session session;
    private final HBox hBox;
    private final TextField textField;
    private final Button btn;
    private final Pane placeHolder;
    private final Label errorLabel;
    private final Label customerLabel;
    private Customer customer;

    public AddCustomerView() {
        mainPane = new BorderPane();
        mainPane.setPrefWidth(1366);
        mainPane.setPrefHeight(768);
        Pane topPane = new Pane();
        Pane leftPane = new Pane();
        Pane rightPane = new Pane();
        topPane.setPrefHeight(270);
        leftPane.getStyleClass().add("side-filling-pane");
        rightPane.getStyleClass().add("side-filling-pane");
        mainPane.setTop(topPane);
        mainPane.setLeft(leftPane);
        mainPane.setRight(rightPane);
        VBox vBox = new VBox(12);
        mainPane.setCenter(vBox);

        session = ViewsManager.getInstance().getSession();
        hBox = new HBox(12);
        hBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(hBox);
        hBox.getChildren().add(new Label("Nouveau client :"));
        textField = new TextField();
        textField.setPromptText(I18N.get("name") + " " + I18N.get("Or") + " " + I18N.get("nat_reg_number"));
        placeHolder = new Pane();
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        customerLabel = new Label();

        btn = new Button(I18N.get("valid"));
        btn.setOnAction(event -> tryFill());

        hBox.getChildren().add(textField);
        hBox.getChildren().add(btn);
        HBox.setHgrow(textField, Priority.ALWAYS);
        vBox.getChildren().add(errorLabel);
        Pane pane = new Pane();
        pane.setPrefHeight(80);
        vBox.getChildren().add(pane);

        Button confirmButton = new Button(I18N.get("add_new_customer"));
        BorderPane buttonPane = new BorderPane(confirmButton);
        vBox.getChildren().add(buttonPane);
        confirmButton.setPrefHeight(50);
        confirmButton.setOnAction(event -> {
            if (btn.getText().equals(I18N.get("change")))
                sendWalletCreationRequest();
            else
                errorLabel.setText(I18N.get("valid_name_or_nrn"));
        });
    }

    @Override
    public Pane getView() {
        return mainPane;
    }

    @Override
    public String getTitle() {
        return I18N.get("add_new_customer");
    }

    void tryFill() {
        errorLabel.setText("");
        String id = textField.getText();
        Optional<Customer> optionalCustomer = session.findCustomer(id);
        if (optionalCustomer.isEmpty()) {
            errorLabel.setText(I18N.get("no_match_found"));
            textField.setText("");
        } else {
            Customer customer = optionalCustomer.get();
            fill(customer);
        }
    }

    void fill(Customer customer) {
        this.customer = customer;
        customerLabel.setText(customer.getName());
        hBox.getChildren().remove(textField);
        hBox.getChildren().add(1, customerLabel);
        hBox.getChildren().add(2, placeHolder);
        HBox.setHgrow(placeHolder, Priority.ALWAYS);

        btn.setText(I18N.get("change"));
        btn.setOnAction(event -> unFill());
    }

    void unFill() {
        hBox.getChildren().remove(customerLabel);
        hBox.getChildren().remove(placeHolder);
        hBox.getChildren().add(1, textField);
        HBox.setHgrow(textField, Priority.ALWAYS);

        btn.setText(I18N.get("valid"));
        btn.setOnAction(event -> tryFill());

        customer = null;
    }

    private boolean customerWithoutAccount(){
        Wallet wallet = session.getWalletRegister().getWalletByBank((Bank) session.getUser());
        for (Account account : wallet.getAccounts()){
            if (account.getOwnership().isCustomerInvolved(customer))
                return false;
        }
        return true;
    }

    private void requestSent() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(I18N.get("success"));
        successAlert.setContentText(I18N.get("request_sent"));
        successAlert.setOnCloseRequest(event -> {
            if (btn.getText().equals(I18N.get("change"))) {
                unFill();
                textField.setText("");
            }
            manager.setPreviousView();
        });
        successAlert.showAndWait();
    }

    private void sendWalletCreationRequest(){
        errorLabel.setText("");
        Ownership ownership = new OwnershipBuilder()
                .type(OwnershipType.INDIVIDUAL)
                .owner(customer)
                .build();
        Account account = new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .bank((Bank) session.getUser())
                .iban(manager.getSession().getUnusedValidIban())
                .ownership(ownership)
                .build();
        if (customerWithoutAccount()){
            RestResponse<Boolean> response = manager.getSession().createAccount(account);
            if (response.isSuccess())
                requestSent();
            else
                errorLabel.setText(I18N.get("Error") + response.getMessage());
        }else
            errorLabel.setText(I18N.get("customer_already_exists"));
    }
}
