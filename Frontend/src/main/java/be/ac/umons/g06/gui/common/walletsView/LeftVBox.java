package be.ac.umons.g06.gui.common.walletsView;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class LeftVBox extends VBox {

    private final ObjectProperty<Button> focusedButton;

    LeftVBox(Collection<String> walletsNames) {
        super();
        focusedButton = new SimpleObjectProperty<>();
        setPrefHeight(Double.MIN_VALUE);
        setSpacing(5);
        for (String s: walletsNames) {
            Button btn = new Button(s);
            getChildren().add(btn);
            btn.setOnAction(event -> focusedButton.set(btn));
            btn.setPrefHeight(40);
            btn.setMinHeight(40);
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        focusedButton.addListener((ObservableValue<? extends Button> observable, Button oldBtn, Button newBtn) -> {
            if (oldBtn != null)
                oldBtn.setEffect(null);
            newBtn.setEffect(new InnerShadow());
        });
    }

    ObjectProperty<Button> getFocusedBtnProperty() {
        return focusedButton;
    }

}
