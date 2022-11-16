package be.ac.umons.g06.gui.common.chartsView;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ChartsView extends InnerView {

    private final BorderPane mainPane;

    public ChartsView() {
        mainPane = new BorderPane();
        mainPane.setPrefWidth(1366);
        mainPane.setPrefHeight(768);

        VBox vBox = new VBox();
        mainPane.setCenter(vBox);

        Chart chart = new Chart(vBox);
        vBox.getChildren().add(new ChartsParametersPane(chart));
    }

    @Override
    public Pane getView() {
        return mainPane;
    }

    @Override
    protected String getTitle() {
        return I18N.get("accounts_evolution_visual");
    }
}
