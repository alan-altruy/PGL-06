package be.ac.umons.g06.gui.employee_app;

import be.ac.umons.g06.gui.common.LoginView;
import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.model.RestResponse;
import be.ac.umons.g06.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeLoginView extends LoginView {

    @Override
    protected void login() {
        RestResponse<User> response = manager.getSession().login("", nameTextField.getText(), passwordField.getText());
        if (response.isSuccess()) {
            manager.setView(ViewName.MENU_VIEW);
            manager.userChanged();
        }
        else {
            errorLabel.setText(I18N.get(response.getMessage()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        super.initialize(url, resources);
        nameLabel.setText(I18N.get("bank"));
        nameTextField.setPromptText("Swift");
        vBox.getChildren().remove(6, 8);
        vBox.getChildren().remove(vBox.getChildren().size() - 2);
        vBox.getChildren().remove(vBox.getChildren().size() - 2);
        createUserButton.setOnAction(event -> manager.setView(ViewName.SIGNUP_VIEW));
    }
}
