package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public abstract class InnerView {

    protected ViewsManager manager;

    protected abstract Pane getView();

    protected abstract String getTitle();

    protected boolean[] enableTopButtons() {
        return new boolean[]{true, true, true};
    }

    protected String getFXMLPath() {
        return null;
    }

    public InnerView() {
        manager = ViewsManager.getInstance();
        if (getFXMLPath() != null)
            initWithFXML();
    }

    protected final void initWithFXML() {
        URL res = getClass().getClassLoader().getResource(getFXMLPath());
        try {
            FXMLLoader loader = new FXMLLoader(res, I18N.getBundle());
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
