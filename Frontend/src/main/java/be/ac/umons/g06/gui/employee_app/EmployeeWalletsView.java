package be.ac.umons.g06.gui.employee_app;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.walletsView.WalletsView;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.model.CustomerRegister;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeWalletsView extends WalletsView {

    @Override
    protected String getTitle() {
        return I18N.get("accounts");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initLeftVBox(manager.getSession().getWalletRegister().getAllCustomerNames());
        leftLabel.setText(I18N.get("customers"));
        leftButton.setText((I18N.get("add_new_customer")));
        leftButton.setOnAction(event -> manager.setView(ViewName.ADD_CUSTOMER_VIEW));
    }

    @Override
    protected void leftVBoxChanged(String text) {
        CustomerRegister customerRegister = manager.getSession().getCustomerRegister();
        if (customerRegister.findCustomer(text).isPresent())
            setRightPaneContent(text, manager.getSession().getWalletRegister().getWalletByCustomer(customerRegister.findCustomer(text).get()).getAccounts());
    }
}
