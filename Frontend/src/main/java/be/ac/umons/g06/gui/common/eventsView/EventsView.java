package be.ac.umons.g06.gui.common.eventsView;

import be.ac.umons.g06.gui.common.InnerView;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.event.Event;

public class EventsView extends InnerView {

    private final BorderPane eventsPane;
    private final VBox vBox;

    public EventsView() {
        eventsPane = new BorderPane();
        eventsPane.setPrefHeight(768);
        eventsPane.setPrefHeight(1366);

        vBox = new VBox();
        eventsPane.setCenter(vBox);

        vBox.getChildren().add(new EventsParametersPane(this));

        vBox.getChildren().add(new Pane());
    }

    void setTable(TableView<? extends Event> table) {
        vBox.getChildren().remove(vBox.getChildren().size() - 1);
        vBox.getChildren().add(table);
    }

    @Override
    public Pane getView() {
        return eventsPane;
    }

    @Override
    protected String getTitle() {
        return I18N.get("events");
    }
}
