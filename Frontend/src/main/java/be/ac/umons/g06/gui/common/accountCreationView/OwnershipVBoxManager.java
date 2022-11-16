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
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.session.Session;
import be.ac.umons.g06.model.ownership.Ownership;
import be.ac.umons.g06.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.model.ownership.OwnershipType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class OwnershipVBoxManager {
    private final Session session;
    private final VBox ownershipVBox;
    private final Label errorLabel;
    private OwnershipBuilder builder;
    private OwnershipType type;
    private YoungOwnershipHBox youngHBox;
    private YoungOwnershipHBox supervisorHBox;
    private Set<Customer> customers;
    private Customer mainCustomer;

    public OwnershipVBoxManager(VBox vBox, Label errorLabel) {
        session = ViewsManager.getInstance().getSession();
        ownershipVBox = vBox;
        type = OwnershipType.INDIVIDUAL;
        this.errorLabel = errorLabel;
    }

    Ownership buildOwnership() {
        if (type.equals(OwnershipType.YOUNG)) {
            if (youngHBox.getCustomer() == null || supervisorHBox.getCustomer() == null) {
                errorLabel.setText(I18N.get("young_case_no_filled"));
                return null;
            }
            else {
                builder.supervisor(supervisorHBox.getCustomer());
                builder.young(youngHBox.getCustomer());
            }
        }
        if (type.equals(OwnershipType.INDIVIS) || type.equals(OwnershipType.JOIN))
            for (Customer owner: customers)
                builder.owner(owner);
        try {
            return builder.build();
        } catch (AssertionError | NullPointerException e) {
            errorLabel.setText(I18N.get("error_owners_specification"));
            return null;
        }
    }

    void setMainCustomer(Customer customer) {
        mainCustomer = customer;
        update();
    }

    void setOwnershipType(OwnershipType ownershipType) {
        type = ownershipType;
        update();
    }

    private void update() {
        builder = new OwnershipBuilder()
                .type(type)
                .owner(mainCustomer);
        ownershipVBox.getChildren().clear();
        switch (type) {
            case INDIVIDUAL:
                ownershipVBox.getChildren().add(new Label(I18N.get("owner") + " : " + mainCustomer.getName()));
                break;
            case YOUNG:
                youngHBox = new YoungOwnershipHBox(true, errorLabel);
                supervisorHBox = new YoungOwnershipHBox(false, errorLabel);
                ownershipVBox.getChildren().add(youngHBox);
                ownershipVBox.getChildren().add(supervisorHBox);
                if (!mainCustomer.isAdult()) {
                    youngHBox.fill(mainCustomer);
                    youngHBox.lock();
                }
                else {
                    supervisorHBox.fill(mainCustomer);
                    supervisorHBox.lock();
                }
                break;
            case JOIN:
            case INDIVIS:
                customers = new HashSet<>();
                customers.add(mainCustomer);
                ownershipVBox.getChildren().add(new Label(I18N.get("owner") + " : " + mainCustomer.getName()));
                HBox addOwnerHBox = new HBox();
                addOwnerHBox.setAlignment(Pos.CENTER_LEFT);
                addOwnerHBox.setSpacing(12);
                TextField addOwnerTextField = new TextField();
                addOwnerTextField.setPromptText(I18N.get("nat_reg_number"));
                HBox.setHgrow(addOwnerTextField, Priority.ALWAYS);
                addOwnerHBox.getChildren().add(addOwnerTextField);

                Button addOwnerButton = new Button(I18N.get("add_owner"));
                addOwnerButton.setOnAction(event -> tryAddNewOwner(addOwnerTextField));
                addOwnerHBox.getChildren().add(addOwnerButton);
                ownershipVBox.getChildren().add(addOwnerHBox);
        }
    }

    // TO DO : check that input is correct (not already in list), and user exists, etc.
    private void tryAddNewOwner(TextField textField) {
        errorLabel.setText("");
        String id = textField.getText();
        Optional<Customer> optionalCustomer = session.findCustomer(id);
        if (optionalCustomer.isEmpty())
            errorLabel.setText(I18N.get("no_match_found"));
        else {
            Customer customer = optionalCustomer.get();
            if (!customers.contains(customer))
                addNewOwner(customer);
            else
                errorLabel.setText(I18N.get("customer_present_only_once"));
        }
        textField.setText("");
    }

    private void addNewOwner(Customer newOwner) {
        customers.add(newOwner);

        HBox newOwnerHbox = new HBox();
        newOwnerHbox.setAlignment(Pos.CENTER_LEFT);
        newOwnerHbox.getChildren().add(new Label(I18N.get("owner") + " : " + newOwner.getName()));
        Pane pane = new Pane();
        newOwnerHbox.getChildren().add(pane);
        HBox.setHgrow(pane, Priority.ALWAYS);
        Button deleteOwnerButton = new Button(I18N.get("delete"));
        deleteOwnerButton.setOnAction(event -> {
            ownershipVBox.getChildren().remove(newOwnerHbox);
            customers.remove(newOwner);
        });
        newOwnerHbox.getChildren().add(deleteOwnerButton);
        ownershipVBox.getChildren().add(ownershipVBox.getChildren().size()-1, newOwnerHbox);
    }
}
