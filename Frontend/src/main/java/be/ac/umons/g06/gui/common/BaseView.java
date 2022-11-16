package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

/**
 * The javafx component that will remain as long as the application is running. It defines the top bar of the GUI (title
 * + some buttons on the right, the bottom bar (with connected user Label on left and dateTime Label on right), and his
 * center must be filled with an InnerView. This way, we can change the center to show for example another menu, but the
 * GUI elements in the BaseView remain all the time.
 */
public class BaseView implements Initializable {

    private final ViewsManager manager;

    @FXML
    private Label titleLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    public BorderPane mainPane;

    @FXML
    public BorderPane contentPane;

    @FXML
    private HBox alignmentHBox;

    @FXML
    private HBox baseHBox;

    @FXML
    private Button settingsButton;

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    public BaseView(ViewsManager manager) {
        this.manager = manager;
        try {
            URL res = getClass().getClassLoader().getResource("fxml/base_view.fxml");
            FXMLLoader loader = new FXMLLoader(res, I18N.getBundle());
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used for testing
     */
    public BorderPane getContentPane() {
        return contentPane;
    }

    /**
     * Used for testing
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     * Used for testing
     */
    public Button getSettingsButton() {
        return settingsButton;
    }

    /**
     * Used for testing
     */
    public Button getBackButton() {
        return backButton;
    }

    /**
     * Used for testing
     */
    public Button getLogoutButton() {
        return logoutButton;
    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    /**
     * Change the content at the center of the GUI
     * @param newInnerView The new InnerView that will be displayed
     */
    public void updateContent(InnerView newInnerView) {
        contentPane.setCenter(newInnerView.getView());
        setTopButtonsEnabled(newInnerView.enableTopButtons());
        updateTopButtonsText();
        updateUsernameLabel();
        titleLabel.setText(newInnerView.getTitle());
    }

    /**
     * Enable (or disable) the top buttons. If a disabled button has no enabled button on his left, it is not displayed.
     * @param values An array of 3 booleans, each boolean is used to enable one of the buttons (from left to right)
     */
    private void setTopButtonsEnabled(boolean[] values) {
        assert values.length == 3;

        settingsButton.setVisible(values[0]);

        backButton.setVisible(settingsButton.isVisible() || values[1]);
        backButton.setDisable(!values[1]);

        logoutButton.setVisible(backButton.isVisible() || values[2]);
        logoutButton.setDisable(!values[2]);
    }

    private void updateTopButtonsText() {
        settingsButton.setText(I18N.get("settings"));
        backButton.setText(I18N.get("back"));
        logoutButton.setText(I18N.get("logout"));
    }

    public void updateUsernameLabel() {
        usernameLabel.setText(manager.getUserName());
    }

    private void initDateLabel() {
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 500 ),
                        event -> dateLabel.setText(LocalDateTime.now().format(I18N.getDateTimePattern(FormatStyle.MEDIUM)))
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        settingsButton.setOnAction(event -> manager.setView(ViewName.SETTINGS_VIEW));
        backButton.setOnAction(event -> manager.setPreviousView());
        logoutButton.setOnAction(event -> manager.closeSession());
        alignmentHBox.prefWidthProperty().bind(baseHBox.widthProperty());

        initDateLabel();
    }
}
