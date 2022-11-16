package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.walletsView.WalletsView;
import be.ac.umons.g06.gui.common.util.I18N;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerWalletsView extends WalletsView {
    @Override
    protected String getTitle() {
        return I18N.get("wallets");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initLeftVBox(manager.getSession().getWalletRegister().getAllBanksNames());
        leftLabel.setText(I18N.get("wallets"));
        leftButton.setText((I18N.get("open_new_wallet")));
        leftButton.setOnAction(event->manager.setView(ViewName.OPEN_WALLET_VIEW));
    }

    @Override
    protected void leftVBoxChanged(String text) {
        setRightPaneContent(text, manager.getSession().getWalletRegister().getWalletByBankName(text).getAccounts());
    }
}
