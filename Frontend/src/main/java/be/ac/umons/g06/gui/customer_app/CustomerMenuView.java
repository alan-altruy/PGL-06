package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.MenuView;
import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.util.I18N;

public class CustomerMenuView extends MenuView {

    public CustomerMenuView() {
        super();
        addButton(I18N.get("wallets"), ViewName.WALLETS_VIEW);
        addButton(I18N.get("events"), ViewName.EVENTS_VIEW);
        addButton(I18N.get("accounts_evolution"), ViewName.CHARTS_VIEW);
        addButton(I18N.get("new_transfer"), ViewName.TRANSFER_VIEW);
    }

    @Override
    public String getTitle() {
        return super.getTitle() + ", " + manager.getUserName();
    }
}
