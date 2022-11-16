package be.ac.umons.g06.gui.common.accountView;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.ConfirmAlert;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.account.AccountType;
import be.ac.umons.g06.model.ownership.OwnershipInvolvement;

import java.time.format.FormatStyle;
import java.util.Optional;

public class InfoVBox extends InnerVBox {

    InfoVBox(Account account) {
        super(account);
    }

    @Override
    protected String getTitle() {
        return I18N.get("general_information");
    }

    @Override
    protected void addContent() {
        getChildren().add(Util.getUnderlinedLabel(I18N.get("iban")));
        getChildren().add(new Label(account.prettyPrintIban()));
        getChildren().add(new Separator());

        getChildren().add(Util.getUnderlinedLabel(I18N.get("sold")));
        getChildren().add(new Label(account.getDisplayedBalance() + " " + Util.EURO));
        getChildren().add(new Separator());

        getChildren().add(Util.getUnderlinedLabel(I18N.get(account.getOwnership().getType().toString())));
        for (OwnershipInvolvement involvement : account.getOwnership().getInvolvements())
            getChildren().add(new Label(I18N.get(involvement.getRole()) + " : " + involvement.getCustomer().getName()));
        getChildren().add(new Separator());

        if (account.getCreationDate() != null) {
            getChildren().add(Util.getUnderlinedLabel(I18N.get("creation_date")));
            getChildren().add(new Label(account.getCreationDate().format(I18N.getDatePattern(FormatStyle.MEDIUM))));
            getChildren().add(new Separator());
        }

        addSpecificContent(account);
        addInfoButtons();
        addActionsButtons();
    }

    private void addSpecificContent(Account account) {
        if (account.getType() == AccountType.SAVINGS_ACCOUNT) {
            getChildren().add(Util.getUnderlinedLabel(I18N.get("linked_current_account")));
            getChildren().add(new Label(Account.prettyPrintIban(account.getLinkedAccountIban())));
            getChildren().add(new Separator());
        }
    }

    private void addInfoButtons() {
        getChildren().add(Util.getUnderlinedLabel(I18N.get("other_information")));
        HBox hBox = new HBox(8);
        Button chartsButton = new Button(I18N.get("evolution"));
        Button requestsButton = new Button(I18N.get("requests"));
        Button operationsButton = new Button(I18N.get("operations"));

        chartsButton.setOnAction(event -> ViewsManager.getInstance().setView(ViewName.CHARTS_VIEW));
        requestsButton.setOnAction(event -> ViewsManager.getInstance().setView(ViewName.EVENTS_VIEW));
        operationsButton.setOnAction(event -> ViewsManager.getInstance().setView(ViewName.EVENTS_VIEW));

        hBox.getChildren().add(requestsButton);
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(operationsButton);
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(chartsButton);
        getChildren().add(hBox);
        getChildren().add(new Separator());
    }

    private void addActionsButtons() {
        getChildren().add(Util.getUnderlinedLabel(I18N.get("actions")));
        HBox hBox = new HBox(8);
        Button disableButton = new Button(I18N.get("disable_account"));
        Button deleteButton = new Button(I18N.get("close_account"));
        if (account.getBalance() < 0)
            deleteButton.setDisable(true);

        disableButton.setOnAction(event -> confirmDisable());
        deleteButton.setOnAction(event -> confirmDelete());

        hBox.getChildren().add(disableButton);
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(deleteButton);
        getChildren().add(hBox);
    }

    private void confirmDisable() {
        ConfirmAlert confirmAlert = new ConfirmAlert("confirm", "disable_account", "confirm_disable_account");
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && !result.get().getButtonData().isCancelButton())
            ViewsManager.getInstance().getSession().disableAccount(account);
    }

    private void confirmDelete() {
        String content = "confirm_delete_account.balance_not_nul";
        if (account.getBalance() > 0) {
            content = "confirm_delete_account.balance_not_nul";
        }
        ConfirmAlert confirmAlert = new ConfirmAlert("warning", "delete_account", content);

        Button yesButton = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setDefaultButton(false);
        Button noButton = (Button) confirmAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
        noButton.setDefaultButton(true);

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && !result.get().getButtonData().isCancelButton())
            ViewsManager.getInstance().getSession().closeAccount(account);
    }
}
