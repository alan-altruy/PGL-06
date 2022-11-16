package be.ac.umons.g06.gui.employee_app;

import be.ac.umons.g06.gui.common.MenuView;
import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.util.I18N;

public class EmployeeMenuView extends MenuView {

    EmployeeMenuView() {
        super();
        addButton(I18N.get("accounts"), ViewName.WALLETS_VIEW);
        addButton(I18N.get("events"), ViewName.EVENTS_VIEW);
        addButton(I18N.get("accounts_evolution"), ViewName.CHARTS_VIEW);
    }
}
