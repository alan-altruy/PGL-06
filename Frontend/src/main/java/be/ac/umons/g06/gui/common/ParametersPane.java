package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.AccountSelector;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.session.Session;
import be.ac.umons.g06.session.SessionType;

import java.time.LocalDate;
import java.util.*;

public abstract class ParametersPane extends TitledPane {

    protected Session session;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected final AccountSelector accountSelector;

    public ParametersPane(String title, Node content) {
        super(title, content);
        setPrefWidth(1366);
        setAnimated(false);

        session = ViewsManager.getInstance().getSession();

        startDate = LocalDate.now().minusYears(1);
        endDate = LocalDate.now();

        accountSelector = new AccountSelector();
    }

    protected VBox getDateSelector() {
        HBox dateHBox = new HBox(12);
        dateHBox.setAlignment(Pos.CENTER_LEFT);

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        dateHBox.getChildren().add(new Label(I18N.get("between")));
        dateHBox.getChildren().add(startDatePicker);
        dateHBox.getChildren().add(new Label(I18N.get("and")));
        dateHBox.getChildren().add(endDatePicker);

        startDatePicker.setOnAction(event -> startDate = startDatePicker.getValue());
        endDatePicker.setOnAction(event -> endDate = endDatePicker.getValue());

        startDatePicker.setEditable(false);
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(endDate) > 0 || date.compareTo(LocalDate.now()) > 0);
            }
        });

        endDatePicker.setEditable(false);
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(startDate) < 0 || date.compareTo(LocalDate.now()) > 0);
            }
        });

        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
        return buildStandardVBox("date", dateHBox);
    }

    protected VBox getUserSelector() {
        List<String> names = new ArrayList<>();
        String title;
        if (session.getType().equals(SessionType.BANK)) {
            names.addAll(session.getWalletRegister().getAllCustomerNames());
            names.add(0, I18N.get("all_customers"));
            title = "customer";
        } else {
            names.addAll(session.getWalletRegister().getAllBanksNames());
            names.add(0, I18N.get("all_banks"));
            title = "bank";
        }
        return getUserVBox(names, title);
    }

    private VBox getUserVBox(List<String> values, String title) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(values);

        comboBox.setOnAction(event -> {
            if (comboBox.getSelectionModel().getSelectedIndex() == 0)
                accountSelector.setAll();
            else
                setUserChoice(comboBox.getValue());
        });

        comboBox.getSelectionModel().selectFirst();
        comboBox.fireEvent(new ActionEvent());

        return buildStandardVBox(title, comboBox, false);
    }

    private void setUserChoice(String name) {
        if (session.getType().equals(SessionType.BANK))
            accountSelector.setCustomer(session.findCustomer(name).get());
        else
            accountSelector.setBank(session.getBankRegister().getBankByName(name).get());
    }

    protected VBox getAccountSelector() {
        return buildStandardVBox("account", accountSelector.getComboBox(), false);
    }

    protected VBox getSelectedAccountsPane() {
        return buildStandardVBox("selected_accounts", accountSelector.getSelectedAccountsPane());
    }

    protected final Set<Account> getSelectedAccounts() {
        return accountSelector.getSelectedAccounts();
    }

    protected VBox buildStandardVBox(String title, Node node) {
        return buildStandardVBox(title, node, true);
    }

    protected VBox buildStandardVBox(String title, Node node, boolean applyStyle) {
        VBox vBox = new VBox(8);
        vBox.getChildren().add(Util.getUnderlinedLabel(I18N.get(title)));
        vBox.getChildren().add(node);
        if (applyStyle)
            applyStandardStyle(vBox);
        return vBox;
    }

    protected void applyStandardStyle(Node node) {

    }
}
