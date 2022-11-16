package be.ac.umons.g06.gui.common.chartsView;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import be.ac.umons.g06.model.event.Operation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GraphPoints {

    List<GraphPoint> points;
    Granularity granularity;

    public GraphPoints(List<Operation> operationsList, LocalDate startDate, LocalDate endDate, Granularity granularity){
        points = new ArrayList<>();
        this.granularity = granularity;

        LocalDate currentDate = computeStartDate(startDate);
        float currentAmount = 0f;
        int index= 0;
        while (currentDate.compareTo(endDate) <= 0) {
            while (index < operationsList.size() &&
                    currentDate.compareTo(operationsList.get(index).getDateTime().toLocalDate()) > 0) {
                currentAmount += (float) operationsList.get(index).getAmount() / 100;
                index += 1;
            }
            points.add(new GraphPoint(currentAmount, currentDate));
            currentDate = getNextDate(currentDate);
        }
    }

    public LocalDate computeStartDate(LocalDate date) {
        switch (granularity) {
            case DAILY: return date;
            case WEEKLY: return date.with(DayOfWeek.MONDAY);
            case MONTHLY: return date.withDayOfMonth(1);
            default: return date.withDayOfYear(1);
        }
    }

    private LocalDate getNextDate(LocalDate date){
        switch (granularity) {
            case DAILY: return date.plusDays(1);
            case WEEKLY: return date.plusWeeks(1);
            case MONTHLY: return date.plusMonths(1);
            default: return date.plusYears(1);
        }
    }

    public String formatDate(LocalDate localDate) {
        switch (granularity) {
            case DAILY: return localDate.toString();
            case WEEKLY: return I18N.get("charts.week") + localDate.format(DateTimeFormatter.ofPattern("ww yyyy"));
            case MONTHLY: return localDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            default: return localDate.getYear() + "";
        }
    }

    public ObservableList<XYChart.Data<String, Number>> getSeries(){
        ObservableList<XYChart.Data<String, Number>> series = FXCollections.observableArrayList();
        for (GraphPoint point : points)
            series.add(new XYChart.Data<>(formatDate(point.date), point.balance));
        return series;
    }

    private static class GraphPoint {
        private final float balance;
        private final LocalDate date;

        private GraphPoint(float balance, LocalDate date) {
            this.balance = balance;
            this.date = date;
        }
    }
}
