package be.ac.umons.g06.gui.common.eventsView;

import be.ac.umons.g06.gui.common.ViewsManager;
import be.ac.umons.g06.gui.common.util.I18N;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import be.ac.umons.g06.model.Customer;
import be.ac.umons.g06.model.event.Decision;
import be.ac.umons.g06.model.event.Request;
import be.ac.umons.g06.session.SessionType;

public class RequestValidationHBox extends HBox {

    private final Request request;

    RequestValidationHBox(Request request) {
        super(2);
        this.request = request;

        Button acceptButton = new Button(I18N.get("accept"));
        acceptButton.getStyleClass().add("accept-button");
        acceptButton.setOnAction(event -> updateRequest(Decision.ACCEPTED));
        Button denyButton = new Button(I18N.get("reject"));
        denyButton.getStyleClass().add("reject-button");
        denyButton.setOnAction(event -> updateRequest(Decision.REJECTED));

        Decision decision;
        if (ViewsManager.getInstance().getSession().getType().equals(SessionType.BANK))
            decision = request.getBankDecision();
        else
            decision = request.getCustomerDecision((Customer) ViewsManager.getInstance().getSession().getUser());

        switch (decision) {
            case WAITING:
                getChildren().add(acceptButton);
                getChildren().add(denyButton);
                break;
            case ACCEPTED:
                getChildren().add(new Label(I18N.get("ACCEPTED")));
                break;
            case REJECTED:
                getChildren().add(new Label(I18N.get("REJECTED")));
        }
    }

    private void updateRequest(Decision decision) {
        if (ViewsManager.getInstance().getSession().updateRequest(request, decision)) {
            getChildren().clear();
            getChildren().add(new Label(I18N.get(decision.toString())));
        }
    }
}
