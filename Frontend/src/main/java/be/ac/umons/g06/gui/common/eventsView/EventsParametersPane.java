package be.ac.umons.g06.gui.common.eventsView;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.ParametersPane;
import be.ac.umons.g06.gui.common.util.Util;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Event;
import be.ac.umons.g06.model.event.Operation;
import be.ac.umons.g06.model.event.Request;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EventsParametersPane extends ParametersPane {

    private final EventsView eventsView;
    private final FlowPane innerPane;

    private final Set<Decision> selectedStatus;
    private final FileChooser fileChooser;

    private FlowPane mainFlowPane;
    private String eventType;
    private final VBox requestStatusSelector;
    private HBox userAndAccountSelector;
    private VBox selectedAccountsPane;

    private final OperationsTable operationsTable;
    private final RequestsTable requestsTable;

    public EventsParametersPane(EventsView eventsView) {
        super(I18N.get("filters"), new Label());
        setPrefWidth(1366);
        setAnimated(false);

        this.eventsView = eventsView;

        selectedStatus = new HashSet<>();
        requestStatusSelector = getRequestStatusSelector();

        operationsTable = new OperationsTable();
        requestsTable = new RequestsTable();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Export data");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv Files", ".csv"));

        innerPane = getInnerPane();
        setEventType("OPERATIONS");
        setContent(innerPane);
    }

    private FlowPane getInnerPane() {
        mainFlowPane = new FlowPane();
        mainFlowPane.setHgap(10);
        mainFlowPane.setVgap(10);

        mainFlowPane.getChildren().add((getEventTypeSelector()));
        mainFlowPane.getChildren().add(getDateSelector());
        mainFlowPane.getChildren().add(getActionsSelector());
        return mainFlowPane;
    }

    private HBox getUserAndAccountSelector() {
        HBox hBox = new HBox(8);
        applyStandardStyle(hBox);
        hBox.getChildren().add(getUserSelector());
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(getAccountSelector());
        return hBox;
    }

    private VBox getEventTypeSelector() {
        HBox hBox = new HBox(12);

        RadioButton operationRadioButton = new RadioButton(I18N.get("operations"));
        RadioButton requestRadioButton = new RadioButton(I18N.get("requests"));

        operationRadioButton.setSelected(true);

        operationRadioButton.setOnAction(event -> setEventType("OPERATIONS"));
        requestRadioButton.setOnAction(event -> setEventType("REQUESTS"));

        ToggleGroup toggleGroup = new ToggleGroup();
        operationRadioButton.setToggleGroup(toggleGroup);
        requestRadioButton.setToggleGroup(toggleGroup);

        hBox.getChildren().add(operationRadioButton);
        hBox.getChildren().add(requestRadioButton);

        return buildStandardVBox("select", hBox);
    }

    private void setEventType(String eventType) {
        this.eventType = eventType;
        innerPane.getChildren().remove(userAndAccountSelector);
        innerPane.getChildren().remove(selectedAccountsPane);
        innerPane.getChildren().remove(requestStatusSelector);
        if (eventType.equals("REQUESTS")) {
            innerPane.getChildren().add(1, requestStatusSelector);
        } else {
            selectedAccountsPane = getSelectedAccountsPane();
            userAndAccountSelector = getUserAndAccountSelector();
            innerPane.getChildren().add(1, selectedAccountsPane);
            innerPane.getChildren().add(1, userAndAccountSelector);
        }
    }

    private VBox getRequestStatusSelector() {
        HBox hBox = new HBox(12);
        for (Decision decision: Decision.values()) {
            CheckBox checkBox = new CheckBox(I18N.get(decision.toString()));
            checkBox.setSelected(true);
            selectedStatus.add(decision);
            hBox.getChildren().add(checkBox);
            checkBox.setOnAction(event -> {
                if (selectedStatus.contains(decision))
                    selectedStatus.remove(decision);
                else
                    selectedStatus.add(decision);
            });
        }
        return buildStandardVBox("request_status", hBox);
    }

    private TableView<? extends Event> computeTable() {
        if (eventType.equals("REQUESTS")) {
            requestsTable.getItems().clear();
            requestsTable.getItems().addAll(getSelectedRequests());
            return requestsTable;
        } else {
            operationsTable.getItems().clear();
            operationsTable.getItems().addAll(getSelectedOperations());
            return operationsTable;
        }
    }

    private List<Operation> getSelectedOperations() {
        List<Operation> operations = new ArrayList<>();
        Predicate<Operation> dateCondition = operation ->
                operation.getDateTime().compareTo(startDate.atStartOfDay()) >= 0
                        && operation.getDateTime().compareTo(endDate.plusDays(1).atStartOfDay()) < 0 ;
        for (Account account : getSelectedAccounts())
            operations.addAll(session.getOperations(account).stream().filter(dateCondition).collect(Collectors.toList()));
        return operations;
    }

    private List<Request> getSelectedRequests() {
        List<Request> requests = new ArrayList<>();
        for (Request request : session.getRequests())
            if (selectedStatus.contains(request.getGlobalDecision()))
                requests.add(request);
        return requests;
    }

    private VBox getActionsSelector() {
        HBox hBox = new HBox(8);

        Button tableBtn = new Button(I18N.get("view_table"));
        tableBtn.setOnAction(event -> eventsView.setTable(computeTable()));
        Button exportBtn = new Button(I18N.get("export_data"));
        exportBtn.setOnAction(event -> exportData());

        hBox.getChildren().add(tableBtn);
        hBox.getChildren().add(Util.getCustomSeparator());
        hBox.getChildren().add(exportBtn);

        return buildStandardVBox("actions", hBox);
    }

    private void exportData() {
        try {
            fileChooser.setInitialFileName("test.csv");
            File file = fileChooser.showSaveDialog(ViewsManager.getStage());
            if (file != null)
                writeCSV(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void writeCSV(File file) {
        try {
            Writer writer = new BufferedWriter(new FileWriter(file));
            if (eventType.equals("REQUESTS")) {
                for (Request request : getSelectedRequests()) {
                    String text = request.getStartDateTime() + ","+ request.getEndDateTime()+ "," + request.getGlobalDecision() + "," +
                            request.getAmount()+ ","+ request.getAccountIban() + "," + request.getDescription() + "\n";
                    writer.write(text);
                }
            }
            else {
                for (Operation operation: getSelectedOperations()) {
                    String text = operation.getDateTime() + ","+ operation.getAccountIban()+ "," + operation.getType() + "," +
                            operation.getAmount()+ ","+ operation.getDescription() + "\n";
                    writer.write(text);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void applyStandardStyle(Node node) {
        node.getStyleClass().add("filters-field");
    }
}
