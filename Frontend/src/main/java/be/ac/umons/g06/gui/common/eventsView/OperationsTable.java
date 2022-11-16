package be.ac.umons.g06.gui.common.eventsView;

import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.event.Operation;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.FormatStyle;

public class OperationsTable extends TableView<Operation> {

    public OperationsTable() {
        super();
        VBox.setVgrow(this, Priority.ALWAYS);

        TableColumn<Operation, LocalDateTime> dateColumn = new TableColumn<>(I18N.get("date"));
        dateColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getDateTime()));
        dateColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(dateTime.format(I18N.getDateTimePattern(FormatStyle.SHORT)));
                }
            }
        });

        TableColumn<Operation, String> accountColumn = new TableColumn<>(I18N.get("account"));
        accountColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getAccountIban()));

        TableColumn<Operation, String> typeColumn = new TableColumn<>(I18N.get("type"));
        typeColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(I18N.get(param.getValue().getType().toString())));

        DecimalFormat dfZero = new DecimalFormat("0.00");
        TableColumn<Operation, Integer> amountColumn = new TableColumn<>(I18N.get("amount"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(dfZero.format(amount.floatValue()/100) + " " + Util.EURO);
                }
            }
        });

        TableColumn<Operation, String> descriptionColumn = new TableColumn<>(I18N.get("description"));
        descriptionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(
                param.getValue().getDescription() == null ? "" : Util.convertDescription(param.getValue().getDescription())));

        getColumns().add(dateColumn);
        getColumns().add(accountColumn);
        getColumns().add(typeColumn);
        getColumns().add(amountColumn);
        getColumns().add(descriptionColumn);
    }
}
