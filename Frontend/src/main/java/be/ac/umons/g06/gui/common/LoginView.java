package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class LoginView extends InnerView implements Initializable {

    @FXML
    private BorderPane loginPane;

    @FXML
    protected VBox vBox;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    protected Label nameLabel;

    @FXML
    protected TextField nameTextField;

    @FXML
    protected TextField nrnTextField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    protected Label errorLabel;

    @FXML
    protected Button createUserButton;

    @FXML
    protected Button offlineButton;

    abstract protected void login();

    @Override
    protected boolean[] enableTopButtons() {
        return new boolean[]{false, false, false};
    }

    public ComboBox<String> getLanguageComboBox() {
        return languageComboBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getNrnTextField() {
        return nrnTextField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Button getCreateUserButton() {
        return createUserButton;
    }

    @Override
    public Pane getView() {
        return loginPane;
    }

    @Override
    public String getTitle() {
        return I18N.get("login");
    }

    @Override
    protected String getFXMLPath() {
        return "fxml/login_view.fxml";
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        offlineButton.setVisible(false);
        languageComboBox.getItems().setAll(I18N.getSupportedLanguages());
        languageComboBox.setValue(I18N.getLocaleName(I18N.getLocale()));
        languageComboBox.setOnAction((event) -> {
            I18N.setLocale(I18N.getSupportedLocales().get(languageComboBox.getSelectionModel().getSelectedIndex()));
            manager.languageChanged();
        });

        loginButton.setOnAction(event -> login());
        offlineButton.setOnAction(event -> {
            manager.getSession().setOffline();
            login();
        });
    }
}
