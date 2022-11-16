package be.ac.umons.g06.gui.customer_app;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import be.ac.umons.g06.model.RestResponse;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignupView extends InnerView implements Initializable {

    @FXML
    private BorderPane signupPane;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField nrnTextField;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button registerButton;


    private Alert successAlert;

    public ComboBox<String> getLanguageComboBox() {
        return languageComboBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getNrnTextField() {
        return nrnTextField;
    }

    public DatePicker getBirthdatePicker() {
        return birthdatePicker;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    private boolean checkUsername() {
        return nameTextField.getText().length() > 2;
    }

    private boolean checkNRN() {
        return nrnTextField.getText().length() > 2;
    }

    private boolean checkBirthdate() {
        return birthdatePicker.getValue() != null;
    }

    private boolean checkPasswords() {
        return passwordField.getText().length() > 2 && passwordField.getText().equals(confirmPasswordField.getText());
    }

    private void register() {
        errorLabel.setText("");
        if (!checkUsername()) {
            errorLabel.setText(I18N.get("signup.problem_username"));
            return;
        }
        if (!checkNRN()) {
            errorLabel.setText(I18N.get("signup.problem_nrn"));
            return;
        }
        if (!checkBirthdate()) {
            errorLabel.setText(I18N.get("signup.problem_birthdate"));
            return;
        }
        if (!checkPasswords()) {
            errorLabel.setText(I18N.get("signup.problem_passwords"));
            return;
        }

        RestResponse<Boolean> response = manager.getSession().signup(
                nameTextField.getText(),
                nrnTextField.getText(),
                birthdatePicker.getValue(),
                passwordField.getText());

        if (response.isSuccess())
            successAlert.showAndWait();
        else
            errorLabel.setText(I18N.get(response.getMessage()));
    }

    @Override
    protected boolean[] enableTopButtons() {
        return new boolean[]{false, true, false};
    }

    @Override
    public Pane getView() {
        return signupPane;
    }

    @Override
    public String getTitle() {
        return I18N.get("register");
    }

    @Override
    public String getFXMLPath() {
        return "fxml/signup_view.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle(I18N.get("success"));
        successAlert.setContentText(I18N.get("registration_complete"));
        successAlert.setOnCloseRequest(event -> {
            manager.setView(ViewName.MENU_VIEW);
            manager.userChanged();
        });

        birthdatePicker.setEditable(false);
        birthdatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) >= 0);
            }
        });

        languageComboBox.getItems().setAll(I18N.getSupportedLanguages());
        languageComboBox.setValue(I18N.getLocaleName(I18N.getLocale()));
        languageComboBox.setOnAction((event) -> {
            I18N.setLocale(I18N.getSupportedLocales().get(languageComboBox.getSelectionModel().getSelectedIndex()));
            manager.languageChanged();
        });

        registerButton.setText(I18N.get("register"));
        registerButton.setOnAction(event -> register());
    }
}
