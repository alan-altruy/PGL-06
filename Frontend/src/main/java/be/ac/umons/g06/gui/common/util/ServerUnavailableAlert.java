package be.ac.umons.g06.gui.common.util;

import javafx.scene.control.Alert;

public class ServerUnavailableAlert extends Alert {
    public ServerUnavailableAlert() {
        super(AlertType.ERROR);
        setTitle(I18N.get("error"));
        setHeaderText(I18N.get("server_unavailable"));
        setContentText(I18N.get("server_unavailable_msg"));
    }
}
