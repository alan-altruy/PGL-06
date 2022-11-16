package be.ac.umons.g06.gui.common.accountView;

import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import be.ac.umons.g06.model.account.Account;

public abstract class InnerVBox extends VBox {

    protected Account account;

    protected InnerVBox(Account account) {
        super();
        this.account = account;

        getStyleClass().add("filters-field");
        setSpacing(8);
        getChildren().add(getTitlePane());
        getChildren().add(new Separator());
        addContent();
    }

    private BorderPane getTitlePane() {
        BorderPane titlePane = new BorderPane();
        titlePane.getStyleClass().add("subtitle-borderpane");
        titlePane.setCenter(new Label(getTitle()));
        return titlePane;
    }

    protected abstract String getTitle();

    protected abstract void addContent();
}
