package be.ac.umons.g06.gui.common.walletsView;

import be.ac.umons.g06.gui.common.ViewName;
import be.ac.umons.g06.gui.common.InnerView;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import be.ac.umons.g06.model.account.Account;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public abstract class WalletsView extends InnerView implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    protected ScrollPane leftScrollPane;

    @FXML
    protected ScrollPane accountsScrollPane;

    @FXML
    protected Label accountsTitleLabel;

    @FXML
    protected Button leftButton;

    @FXML
    protected Button openAccountButton;

    @FXML
    protected Label leftLabel;


    @Override
    public Pane getView() {
        return mainPane;
    }

    @Override
    public String getFXMLPath() {
        return "fxml/wallets_view.fxml";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        leftScrollPane.pannableProperty().set(true);
        leftScrollPane.fitToWidthProperty().set(true);

        accountsScrollPane.pannableProperty().set(true);
        accountsScrollPane.fitToWidthProperty().set(true);

        openAccountButton.setOnAction(event -> manager.setView(ViewName.ACCOUNT_CREATION_VIEW));
    }

    protected void setRightPaneContent(String text, Collection<Account> accounts) {
        accountsScrollPane.setContent(new AccountsVBox(manager, accounts));
        accountsTitleLabel.setText(text);
    }

    protected void initLeftVBox(Collection<String> strings) {
        LeftVBox leftVBox = new LeftVBox(strings);
        leftScrollPane.setContent(leftVBox);
        leftVBox.getFocusedBtnProperty().addListener(
                (ObservableValue<? extends Button> observable, Button oldBtn, Button newBtn) -> {
                    leftVBoxChanged(newBtn.getText());
                });

        if (!leftVBox.getChildren().isEmpty())
            leftVBox.getFocusedBtnProperty().set((Button) leftVBox.getChildren().get(0));

    }

    protected abstract void leftVBoxChanged(String text);
}
