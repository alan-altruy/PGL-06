package be.ac.umons.g06.gui.common.chartsView;

import be.ac.umons.g06.gui.common.ParametersPane;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChartsParametersPane extends ParametersPane {

    private final Chart chart;

    private Granularity granularity;

    public ChartsParametersPane(Chart chart) {
        super(I18N.get("settings"), new Label());

        granularity = Granularity.DAILY;

        this.chart = chart;
        setContent(getInnerPane());
    }

    private FlowPane getInnerPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);

        flowPane.getChildren().add(getGranularitySelector());
        flowPane.getChildren().add(getDateSelector());
        HBox hBox = new HBox(8);
        applyStandardStyle(hBox);
        hBox.getChildren().add(getUserSelector());
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(getAccountSelector());
        flowPane.getChildren().add(hBox);
        flowPane.getChildren().add(getSelectedAccountsPane());
        flowPane.getChildren().add(getActionsSelector());

        return flowPane;
    }

    void setGranularity(Granularity granularity) {
        this.granularity = granularity;
    }

    private VBox getGranularitySelector() {
        HBox hBox = new HBox(12);
        ToggleGroup toggleGroup = new ToggleGroup();

        for (Granularity granularity : Granularity.values()) {
            RadioButton btn = new RadioButton(I18N.get(granularity.toString()));
            btn.setOnAction(event -> setGranularity(granularity));
            btn.setToggleGroup(toggleGroup);
            hBox.getChildren().add(btn);
        }
        ((RadioButton) hBox.getChildren().get(0)).fire();

        return buildStandardVBox("granularity", hBox);
    }

    private VBox getActionsSelector() {
        Button chartBtn = new Button(I18N.get("display"));
        chartBtn.setOnAction(event -> updateChart());

        return buildStandardVBox("actions", chartBtn);
    }

    @Override
    protected void applyStandardStyle(Node node) {
        node.getStyleClass().add("filters-field");
    }

    private void updateChart() {
        chart.update(startDate, endDate, granularity, getSelectedAccounts());
    }
}
