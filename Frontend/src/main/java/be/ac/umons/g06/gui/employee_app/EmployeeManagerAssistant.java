package be.ac.umons.g06.gui.employee_app;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.ManagerAssistant;
import be.ac.umons.g06.gui.common.SettingsView;
import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.accountCreationView.AccountCreationView;
import be.ac.umons.g06.gui.common.accountView.AccountView;
import be.ac.umons.g06.gui.common.chartsView.ChartsView;
import be.ac.umons.g06.gui.common.eventsView.EventsView;

public class EmployeeManagerAssistant implements ManagerAssistant {

    @Override
    public InnerView getViewByName(ViewName name) {
        switch (name) {
            case ACCOUNT_VIEW:
                return new AccountView();
            case EVENTS_VIEW:
                return new EventsView();
            case CHARTS_VIEW:
                return new ChartsView();
            case LOGIN_VIEW:
                return new EmployeeLoginView();
            case MENU_VIEW:
                return new EmployeeMenuView();
            case WALLETS_VIEW:
                return new EmployeeWalletsView();
            case ACCOUNT_CREATION_VIEW:
                return new AccountCreationView();
            case SETTINGS_VIEW:
                return new SettingsView();
            case ADD_CUSTOMER_VIEW:
                return new AddCustomerView();
            default:
                throw new IllegalArgumentException();
        }
    }
}
