package be.ac.umons.g06.gui.common.chartsView;

import be.ac.umons.g06.gui.common.ViewsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.event.Operation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class Chart {

    private final VBox vBox;

    Chart(VBox vBox) {
        this.vBox = vBox;
    }

    public void update(LocalDate startDate, LocalDate endDate, Granularity granularity, Collection<Account> accounts) {
        if (vBox.getChildren().size() > 1)
            vBox.getChildren().remove(1);

        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        for (Account account : accounts){
            List<Operation> operations = ViewsManager.getInstance().getSession().getOperations(account);
            GraphPoints gp = new GraphPoints(operations, startDate, endDate, granularity);
            seriesList.add(new XYChart.Series<>(account.prettyPrintIban(), gp.getSeries()));
        }
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis(), seriesList);
        lineChart.setCreateSymbols(false);
        vBox.getChildren().add(lineChart);
        VBox.setVgrow(lineChart, Priority.ALWAYS);
    }
}
