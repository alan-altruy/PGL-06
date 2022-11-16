package be.ac.umons.g06.gui.common.accountCreationView;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.session.Session;

import java.util.Optional;

class YoungOwnershipHBox extends HBox {
    private final boolean youngCase;
    private Customer customer;
    private final Label customerLabel;
    private final TextField textField;
    private final Pane placeHolder;
    private final Button btn;
    private boolean locked;
    private final Label errorLabel;
    private final Session session;

    YoungOwnershipHBox(boolean youngCase, Label errorLabel) {
        super();
        this.youngCase = youngCase;
        this.errorLabel = errorLabel;
        this.session = ViewsManager.getInstance().getSession();

        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().add(new Label(I18N.get(youngCase ? "young" : "supervisor") + " :"));

        customerLabel = new Label();
        placeHolder = new Pane();
        textField = new TextField();
        textField.setPromptText(I18N.get("nat_reg_number"));

        btn = new Button(I18N.get("valid"));
        btn.setOnAction(event -> tryFill());

        getChildren().add(textField);
        getChildren().add(btn);
        setHgrow(textField, Priority.ALWAYS);
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
            if (youngCase == customer.isAdult()) {
                errorLabel.setText(I18N.get(youngCase ? "young_adult" : "supervisor_not_adult"));
                textField.setText("");
            }
            else
                fill(customer);
        }
    }

    void fill(Customer customer) {
        if (locked)
            return;
        this.customer = customer;
        customerLabel.setText(customer.getName());
        getChildren().remove(textField);
        getChildren().add(1, customerLabel);
        getChildren().add(2, placeHolder);
        setHgrow(placeHolder, Priority.ALWAYS);

        btn.setText(I18N.get("change"));
        btn.setOnAction(event -> unFill());
    }

    void unFill() {
        if (locked)
            return;
        getChildren().remove(customerLabel);
        getChildren().remove(placeHolder);
        getChildren().add(1, textField);
        setHgrow(textField, Priority.ALWAYS);

        btn.setText(I18N.get("valid"));
        btn.setOnAction(event -> tryFill());

        customer = null;
    }

    void lock() {
        locked = true;
        getChildren().remove(btn);
    }

    Customer getCustomer() {
        return customer;
    }
}