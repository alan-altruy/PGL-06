package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.User;
import be.ac.umons.g06.session.SessionType;

import java.time.format.FormatStyle;
import java.util.Optional;

public class SettingsView extends InnerView {

    private final BorderPane mainPane;

    private PasswordField oldPasswordField;

    private PasswordField newPasswordField;

    private PasswordField passwordConfirmationField;

    public SettingsView() {
        mainPane = new BorderPane();
        mainPane.setPrefWidth(1366);
        mainPane.setPrefHeight(768);

        Pane leftPane = new Pane();
        Pane rightPane = new Pane();
        leftPane.getStyleClass().add("side-filling-pane");
        rightPane.getStyleClass().add("side-filling-pane");
        mainPane.setLeft(leftPane);
        mainPane.setRight(rightPane);
        mainPane.setBottom(new Pane());
        mainPane.setTop(new Pane());

        VBox vBox = new VBox(12);
        vBox.setMaxHeight(400);
        vBox.getStyleClass().add("filters-field");
        mainPane.setCenter(vBox);

        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().setAll(I18N.getSupportedLanguages());
        languageComboBox.setValue(I18N.getLocaleName(I18N.getLocale()));
        languageComboBox.setOnAction((event) -> {
            I18N.setLocale(I18N.getSupportedLocales().get(languageComboBox.getSelectionModel().getSelectedIndex()));
            manager.languageChanged();
        });
        vBox.getChildren().add(Util.getUnderlinedLabel(I18N.get("pref_language")));
        vBox.getChildren().add(languageComboBox);

        vBox.getChildren().add(new Separator());
        addUserInfoLabels(vBox);
        vBox.getChildren().add(new Separator());

        Button changePasswordButton = new Button(I18N.get("change_password"));
        changePasswordButton.setOnAction(event -> openChangePasswordDialog());
        vBox.getChildren().add(changePasswordButton);
    }

    private void addUserInfoLabels(VBox vBox) {
        boolean bankApp = manager.getSession().getType().equals(SessionType.BANK);
        User user = manager.getSession().getUser();

        vBox.getChildren().add(Util.getUnderlinedLabel(I18N.get(bankApp ? "swift" : "nat_reg_number")));
        vBox.getChildren().add(new Label(user.getId()));
        vBox.getChildren().add(Util.getUnderlinedLabel(I18N.get("name")));
        vBox.getChildren().add(new Label(user.getName()));

        if (!bankApp) {
            Customer customer = (Customer) manager.getSession().getUser();
            vBox.getChildren().add(Util.getUnderlinedLabel(I18N.get("birthdate")));
            vBox.getChildren().add(new Label(customer.getBirthdate().format(I18N.getDatePattern(FormatStyle.LONG))));
        }
    }

    @Override
    public Pane getView() {
        return mainPane;
    }

    @Override
    public String getTitle() {
        return I18N.get("settings");
    }

    @Override
    protected boolean[] enableTopButtons() {
        return new boolean[]{false, true, true};
    }

    private void openChangePasswordDialog() {
        VBox alertVBox = new VBox(12);
        alertVBox.getChildren().add(new Label(I18N.get("new_password")));
        newPasswordField = new PasswordField();
        alertVBox.getChildren().add(newPasswordField);
        alertVBox.getChildren().add(new Label(I18N.get("confirm_new_password")));
        passwordConfirmationField = new PasswordField();
        alertVBox.getChildren().add(passwordConfirmationField);
        alertVBox.getChildren().add(new Label(I18N.get("confirm_with_old_password")));
        oldPasswordField = new PasswordField();
        alertVBox.getChildren().add(oldPasswordField);

        alertLoop(alertVBox);
    }

    private boolean checkPasswords() {
        return newPasswordField.getText().length() > 2
                && newPasswordField.getText().equals(passwordConfirmationField.getText())
                && (manager.getSession().askForPermission(oldPasswordField.getText()));
    }

    private void alertLoop(VBox alertVBox){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(I18N.get("change_password"));
        alert.getDialogPane().setContent(alertVBox);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            if(checkPasswords())
                manager.getSession().updatePassword(newPasswordField.getText());
            else
                alertLoop((VBox) alert.getDialogPane().getContent());
        }
    }
}
