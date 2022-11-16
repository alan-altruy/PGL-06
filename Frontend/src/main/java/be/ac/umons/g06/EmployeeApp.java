package be.ac.umons.g06;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import be.ac.umons.g06.session.SessionType;

import java.util.Locale;

public class EmployeeApp extends Application {

    private final String BASE_VIEW = "fxml/base_view.fxml";

    @Override
    public void start(Stage primaryStage) {
        try {
            Locale locale = new Locale("fr");

            I18N.setLocale(locale);

            ViewsManager.init(SessionType.BANK, primaryStage);
            Parent root = ViewsManager.getInstance().getBaseView().getMainPane();
            ViewsManager.getInstance().setView(ViewName.LOGIN_VIEW);

            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            //primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("bank_logo.png")));
            primaryStage.setTitle(I18N.get("app_name"));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}