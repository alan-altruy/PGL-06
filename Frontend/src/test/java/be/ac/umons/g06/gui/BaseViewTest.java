package be.ac.umons.g06.gui;

import be.ac.umons.g06.gui.common.*;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.customer_app.CustomerLoginView;
import be.ac.umons.g06.gui.customer_app.CustomerMenuView;
import be.ac.umons.g06.session.SessionType;
import javafx.scene.Parent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;

import static org.testfx.assertions.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ApplicationExtension.class)
class BaseViewTest{

    private static final String e_acute = "\u00e9";
    private static final String e_with_grave = "\u00e8";
    private BaseView baseView;
    private CustomerLoginView loginView;

    @Start
    public void start(Stage primaryStage) {
        try {
            Locale locale = new Locale("fr");
            I18N.setLocale(locale);
            ViewsManager.init(SessionType.CUSTOMER, primaryStage);
            ViewsManager.getInstance().getSession().setOffline();
            Parent root = ViewsManager.getInstance().getBaseView().getMainPane();
            ViewsManager.getInstance().setView(ViewName.LOGIN_VIEW);
            loginView = (CustomerLoginView) ViewsManager.getInstance().getViewsMap().get(ViewName.LOGIN_VIEW);
            baseView = ViewsManager.getInstance().getBaseView();
            if (primaryStage.getScene()==null) {
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void baseViewTest() {
        assertThat(baseView.getTitleLabel().getText()).isEqualTo("Connexion");
        assertThat(baseView.getSettingsButton()).isInvisible();
        assertThat(baseView.getSettingsButton()).isEnabled();
        assertThat(baseView.getSettingsButton()).hasText("Param"+e_with_grave+"tres");
        assertThat(baseView.getBackButton()).isInvisible();
        assertThat(baseView.getBackButton()).isDisabled();
        assertThat(baseView.getBackButton()).hasText("Retour");
        assertThat(baseView.getLogoutButton()).isInvisible();
        assertThat(baseView.getLogoutButton()).isDisabled();
        assertThat(baseView.getLogoutButton()).hasText("D"+e_acute+"connexion");
    }

    @Test
    @Order(2)
    void topButtonsChanges(){
        loginView.getLoginButton().fire();
        baseView.getSettingsButton().fire();
        assertThat(baseView.getSettingsButton()).isInvisible();
        assertThat(baseView.getSettingsButton()).isEnabled();
        assertThat(baseView.getBackButton()).isVisible();
        assertThat(baseView.getBackButton()).isEnabled();
        assertThat(baseView.getLogoutButton()).isVisible();
        assertThat(baseView.getLogoutButton()).isEnabled();
    }

    @Test
    @Order(3)
    void loginAndTopButtonsClicked(){
        loginView.getLoginButton().fire();
        CustomerMenuView menuView = (CustomerMenuView) ViewsManager.getInstance().getViewsMap().get(ViewName.MENU_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(menuView.getView());
        baseView.getSettingsButton().fire();
        SettingsView settingsView = (SettingsView) ViewsManager.getInstance().getViewsMap().get(ViewName.SETTINGS_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(settingsView.getView());
        baseView.getBackButton().fire();
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(menuView.getView());
        baseView.getLogoutButton().fire();
        loginView = (CustomerLoginView) ViewsManager.getInstance().getViewsMap().get(ViewName.LOGIN_VIEW);
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(loginView.getView());
    }
}
