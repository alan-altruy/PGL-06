package be.ac.umons.g06.gui.common.eventsView;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import be.ac.umons.g06.model.event.Request;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;

public class RequestsTable extends TableView<Request> {
    public RequestsTable() {
        super();
        TableColumn<Request, LocalDateTime> startDateColumn = new TableColumn<>(I18N.get("start_date"));
        startDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getStartDateTime()));
        startDateColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime.format(I18N.getDateTimePattern(FormatStyle.SHORT)));
                }
            }
        });

        TableColumn<Request, LocalDateTime> endDateColumn = new TableColumn<>(I18N.get("end_date"));
        endDateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getEndDateTime()));
        endDateColumn.setCellFactory(startDateColumn.getCellFactory());

        TableColumn<Request, String> statusColumn = new TableColumn<>(I18N.get("status"));
        statusColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(I18N.get(param.getValue().getGlobalDecision().toString())));

        TableColumn<Request, String> typeColumn = new TableColumn<>(I18N.get("type"));
        typeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(I18N.get(param.getValue().getType().toString())));

        TableColumn<Request, HBox> validationColumn = new TableColumn<>(I18N.get("your_decision"));
        validationColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(new RequestValidationHBox(param.getValue())));

        TableColumn<Request, String> descriptionColumn = new TableColumn<>(I18N.get("description"));
        descriptionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(I18N.get(param.getValue().getDescription())));

        getColumns().add(startDateColumn);
        getColumns().add(endDateColumn);
        getColumns().add(statusColumn);
        getColumns().add(typeColumn);
        getColumns().add(validationColumn);
        getColumns().add(descriptionColumn);
    }
}
