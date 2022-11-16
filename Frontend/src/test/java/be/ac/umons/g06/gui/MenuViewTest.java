package be.ac.umons.g06.gui;

import be.ac.umons.g06.gui.common.*;
import be.ac.umons.g06.gui.common.chartsView.ChartsView;
import be.ac.umons.g06.gui.common.eventsView.EventsView;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.customer_app.CustomerWalletsView;
import be.ac.umons.g06.gui.customer_app.CustomerLoginView;
import be.ac.umons.g06.gui.customer_app.CustomerMenuView;
import be.ac.umons.g06.gui.customer_app.TransferView;
import be.ac.umons.g06.session.SessionType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.util.Locale;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class MenuViewTest {

    private static final String e_with_grave = "\u00e8";
    private BaseView baseView;
    private CustomerMenuView menuView;

    @Start
    public void start(Stage primaryStage) {
        try {
            Locale locale = new Locale("fr");
            I18N.setLocale(locale);
            ViewsManager.init(SessionType.CUSTOMER, primaryStage);
            ViewsManager.getInstance().getSession().setOffline();
            Parent root = ViewsManager.getInstance().getBaseView().getMainPane();
            ViewsManager.getInstance().setView(ViewName.LOGIN_VIEW);
            CustomerLoginView loginView = (CustomerLoginView) ViewsManager.getInstance()
                    .getViewsMap().get(ViewName.LOGIN_VIEW);
            loginView.getLoginButton().fire();
            menuView = (CustomerMenuView) ViewsManager.getInstance().getViewsMap().get(ViewName.MENU_VIEW);
            baseView = ViewsManager.getInstance().getBaseView();
            if (primaryStage.getScene()==null){
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void menuViewTest() {
        BorderPane mainPane = (BorderPane) menuView.getView();
        VBox vBox = (VBox) mainPane.getCenter();
        Button walletsButton = (Button) vBox.getChildren().get(0);
        assertThat(walletsButton).isVisible();
        assertThat(walletsButton).isEnabled();
        Button eventsButton = (Button) vBox.getChildren().get(1);
        assertThat(eventsButton).isVisible();
        assertThat(eventsButton).isEnabled();
        Button chartsView = (Button) vBox.getChildren().get(2);
        assertThat(chartsView).isVisible();
        assertThat(chartsView).isEnabled();
        Button transferButton = (Button) vBox.getChildren().get(3);
        assertThat(transferButton).isVisible();
        assertThat(transferButton).isEnabled();
    }

    @Test
    void menuButtonsClicked() {
        BorderPane mainPane = (BorderPane) menuView.getView();
        VBox vBox = (VBox) mainPane.getCenter();
        Button walletsButton = (Button) vBox.getChildren().get(0);
        walletsButton.fire();
        CustomerWalletsView walletsView = (CustomerWalletsView) ViewsManager.getInstance().getViewsMap().get(ViewName.WALLETS_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(walletsView.getView());
        baseView.getBackButton().fire();
        Button eventsButton = (Button) vBox.getChildren().get(1);
        eventsButton.fire();
        EventsView eventsView = (EventsView) ViewsManager.getInstance().getViewsMap().get(ViewName.EVENTS_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(eventsView.getView());
        baseView.getBackButton().fire();
        Button chartsButton = (Button) vBox.getChildren().get(2);
        chartsButton.fire();
        ChartsView chartsView = (ChartsView) ViewsManager.getInstance().getViewsMap().get(ViewName.CHARTS_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(chartsView.getView());
        baseView.getBackButton().fire();
        Button transferButton = (Button) vBox.getChildren().get(3);
        transferButton.fire();
        TransferView transferView = (TransferView) ViewsManager.getInstance()
                .getViewsMap().get(ViewName.TRANSFER_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(transferView.getView());
    }
}
