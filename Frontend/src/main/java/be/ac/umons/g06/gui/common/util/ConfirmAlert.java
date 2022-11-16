package be.ac.umons.g06.gui.common.util;

import javafx.scene.control.Alert;

public class ConfirmAlert extends Alert {

    public ConfirmAlert(String title, String header, String content) {
        super(AlertType.CONFIRMATION);
        setTitle(I18N.get(title));
        setHeaderText(I18N.get(header));
        setContentText(I18N.get(content));
    }
}
