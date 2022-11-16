package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.*;
import be.ac.umons.g06.gui.common.accountCreationView.AccountCreationView;
import be.ac.umons.g06.gui.common.accountView.AccountView;
import be.ac.umons.g06.gui.common.chartsView.ChartsView;
import be.ac.umons.g06.gui.common.eventsView.EventsView;

public class CustomerManagerAssistant implements ManagerAssistant {

    @Override
    public InnerView getViewByName(ViewName name) {
        switch (name) {
            case ACCOUNT_VIEW:
                return new AccountView();
            case EVENTS_VIEW:
                return new EventsView();
            case CHARTS_VIEW:
                return new ChartsView();
            case OPEN_WALLET_VIEW:
                return new OpenWalletView();
            case LOGIN_VIEW:
                return new CustomerLoginView();
            case MENU_VIEW:
                return new CustomerMenuView();
            case WALLETS_VIEW:
                return new CustomerWalletsView();
            case ACCOUNT_CREATION_VIEW:
                return new AccountCreationView();
            case SETTINGS_VIEW:
                return new SettingsView();
            case SIGNUP_VIEW:
                return new SignupView();
            case TRANSFER_VIEW:
                return new TransferView();
            default:
                throw new IllegalArgumentException();
        }
    }
}
