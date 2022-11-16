package be.ac.umons.g06.gui;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.customer_app.CustomerLoginView;
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
class CustomerLoginViewTest {

    private static final String e_acute = "\u00e9";
    private static final String c_cedilla = "\u00e7";
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
            if (primaryStage.getScene()==null) {
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Order(1)
    @Test
    void loginViewTest() {
        assertThat(loginView.getLoginButton()).hasText("Connexion");
        assertThat(loginView.getLoginButton()).isVisible();
        assertThat(loginView.getLoginButton()).isEnabled();
        assertThat(loginView.getCreateUserButton()).hasText("Cr"+e_acute+"er un compte");
        assertThat(loginView.getCreateUserButton()).isVisible();
        assertThat(loginView.getCreateUserButton()).isEnabled();
        assertThat(loginView.getNameTextField()).isVisible();
        assertThat(loginView.getNameTextField()).isEnabled();
        assertThat(loginView.getNrnTextField()).isVisible();
        assertThat(loginView.getNrnTextField()).isEnabled();
        assertThat(loginView.getLanguageComboBox()).containsExactlyItems("Fran"+c_cedilla+"ais", "English");
        assertThat(loginView.getLanguageComboBox()).isEnabled();
        assertThat(loginView.getPasswordField()).isVisible();
        assertThat(loginView.getPasswordField()).isEnabled();
        assertThat(loginView.getTitle()).isEqualTo("Connexion");
        assertThat(loginView.getErrorLabel().getText()).isEqualTo("");
    }

    @Order(2)
    @Test
    void languageChangedTest(){
        I18N.setLocale(I18N.getSupportedLocales().get(1));
        ViewsManager.getInstance().languageChanged();
        loginView = (CustomerLoginView) ViewsManager.getInstance().getViewsMap().get(ViewName.LOGIN_VIEW);
        assertThat(loginView.getLoginButton()).hasText("Login");
        assertThat(loginView.getCreateUserButton()).hasText("Create account");
        assertThat(loginView.getTitle()).isEqualTo("Login");
    }
}
