package be.ac.umons.g06.gui.common.walletsView;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.account.Account;

import java.util.Collection;

class AccountsVBox extends VBox {

    private static final int BTN_HEIGHT = 40;
    private final ViewsManager manager;

    AccountsVBox(ViewsManager manager, Collection<Account> accounts) {
        super();
        this.manager = manager;
        setPrefHeight(Double.MIN_VALUE);
        setSpacing(5);

        for (Account account: accounts) {
            getChildren().add(getAccountButton(account));
        }
    }

    private Button getAccountButton(Account account) {
        Button btn = new Button(getDisplayedAccountName(account));
        btn.getStyleClass().add("wallet-account-button");
        btn.setPrefHeight(BTN_HEIGHT);
        btn.setMinHeight(BTN_HEIGHT);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setOnAction(event -> {
            manager.setSelectedAccount(account);
            manager.setView(ViewName.ACCOUNT_VIEW);
        });
        return btn;
    }

    private String getDisplayedAccountName(Account account) {
        return I18N.get(account.getType().toString())
                + " - "
                + account.prettyPrintIban()
                + " - "
                + account.getDisplayedBalance()
                + " "
                + Util.EURO;
    }
}
