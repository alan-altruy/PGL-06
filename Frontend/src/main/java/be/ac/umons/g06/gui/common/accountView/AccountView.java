package be.ac.umons.g06.gui.common.accountView;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.account.Account;

public class AccountView extends InnerView {

    private final BorderPane mainPane;
    private final VBox vBox;

    public AccountView() {
        mainPane = new BorderPane();
        mainPane.setPrefWidth(1366);
        mainPane.setPrefHeight(768);

        vBox = new VBox(12);
        vBox.setPadding(new Insets(8, 0, 0, 0));
        vBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.pannableProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.getStyleClass().add("edge-to-edge");
        mainPane.setCenter(scrollPane);

        Pane leftPane = new Pane();
        Pane rightPane = new Pane();
        leftPane.getStyleClass().add("side-filling-pane");
        rightPane.getStyleClass().add("side-filling-pane");
        mainPane.setLeft(leftPane);
        mainPane.setRight(rightPane);
    }

    private void updateInnerVBoxes() {
        vBox.getChildren().clear();
        vBox.getChildren().add(new InfoVBox(manager.getSelectedAccount()));
    }

    @Override
    protected Pane getView() {
        updateInnerVBoxes();
        return mainPane;
    }

    @Override
    protected String getTitle() {
        Account account = manager.getSelectedAccount();
        return account.getBank().getName() + " - " + I18N.get(account.getType().toString());
    }
}
