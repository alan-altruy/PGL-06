package be.ac.umons.g06.gui;

import be.ac.umons.g06.gui.common.BaseView;
import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.customer_app.CustomerLoginView;
import be.ac.umons.g06.gui.customer_app.SignupView;
import be.ac.umons.g06.session.SessionType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.time.LocalDate;
import java.util.Locale;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class SignupViewTest {

    private static final String c_cedilla = "\u00e7";
    private static final String e_acute = "\u00e9";
    private SignupView signupView;
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
            loginView.getCreateUserButton().fire();
            signupView = (SignupView) ViewsManager.getInstance().getViewsMap().get(ViewName.SIGNUP_VIEW);
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
    void signupViewTest() {
        assertThat(signupView.getRegisterButton()).hasText("S'enregistrer");
        assertThat(signupView.getRegisterButton()).isVisible();
        assertThat(signupView.getRegisterButton()).isEnabled();
        assertThat(signupView.getNameTextField()).isVisible();
        assertThat(signupView.getNameTextField()).isEnabled();
        assertThat(signupView.getNrnTextField()).isVisible();
        assertThat(signupView.getNrnTextField()).isEnabled();
        assertThat(signupView.getLanguageComboBox()).containsExactlyItems("Fran"+c_cedilla+"ais", "English");
        assertThat(signupView.getLanguageComboBox()).isEnabled();
        assertThat(signupView.getPasswordField()).isVisible();
        assertThat(signupView.getPasswordField()).isEnabled();
        assertThat(signupView.getConfirmPasswordField()).isVisible();
        assertThat(signupView.getConfirmPasswordField()).isEnabled();
        assertThat(signupView.getBirthdatePicker()).isEnabled();
        assertThat(signupView.getTitle()).isEqualTo("S'enregistrer");
        assertThat(signupView.getErrorLabel().getText()).isEqualTo("");
        signupView.getRegisterButton().fire();
        assertThat(signupView.getErrorLabel()).hasText("Nom incorrect");
        signupView.getNameTextField().setText("John Smith");
        signupView.getRegisterButton().fire();
        assertThat(signupView.getErrorLabel()).hasText("Num"+e_acute+"ro de registre national incorrect");
        signupView.getNrnTextField().setText("BE153435146");
        signupView.getRegisterButton().fire();
        assertThat(signupView.getErrorLabel()).hasText("Date de naissance incorrecte");
        signupView.getBirthdatePicker().setValue(LocalDate.of(2016, 5, 1));
        signupView.getRegisterButton().fire();
        assertThat(signupView.getErrorLabel()).hasText("Mots de passe incorrects");
        signupView.getPasswordField().setText("password");
        signupView.getConfirmPasswordField().setText("password");
        signupView.getRegisterButton().fire();
        assertThat(signupView.getErrorLabel()).hasText("Le mode offline est activ"+e_acute);
    }

    @Test
    void disabledAndBackButtonsClicked() {
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(signupView.getView());
        baseView.getLogoutButton().fire();
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(signupView.getView());
        baseView.getBackButton().fire();
        assertThat(baseView.getContentPane().getCenter()).isEqualTo(loginView.getView());
    }
}
