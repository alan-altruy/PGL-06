package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class MenuView extends InnerView {

    private final BorderPane mainPane;
    private final VBox vBox;

    protected MenuView() {
        super();
        mainPane = new BorderPane();
        mainPane.setPrefWidth(1366);
        mainPane.setPrefHeight(768);

        mainPane.setTop(new Separator());

        vBox = new VBox(12);
        vBox.setAlignment(Pos.CENTER);
        mainPane.setCenter(vBox);
    }

    protected final void addButton(String text, ViewName viewName) {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-button");
        vBox.getChildren().add(btn);
        btn.setOnAction(event -> manager.setView(viewName));
    }

    @Override
    protected boolean[] enableTopButtons() {
        return new boolean[]{true, false, true};
    }

    @Override
    public Pane getView() {
        return mainPane;
    }

    @Override
    protected String getTitle() {
        return I18N.get("welcome");
    }
}
