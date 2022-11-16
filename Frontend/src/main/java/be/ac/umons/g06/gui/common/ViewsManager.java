package be.ac.umons.g06.gui.common;

import be.ac.umons.g06.gui.common.util.I18N;
import be.ac.umons.g06.gui.common.util.ServerUnavailableAlert;
import be.ac.umons.g06.gui.customer_app.CustomerManagerAssistant;
import be.ac.umons.g06.gui.employee_app.EmployeeManagerAssistant;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import be.ac.umons.g06.model.account.Account;
import be.ac.umons.g06.session.CustomerAppSession;
import be.ac.umons.g06.session.EmployeeAppSession;
import be.ac.umons.g06.session.Session;
import be.ac.umons.g06.session.SessionType;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Singleton class that manages all the GUI.
 */
public final class ViewsManager {

    private final Session session;
    private static Stage stage;
    private final BaseView baseView;
    private final HashMap<ViewName, InnerView> viewsMap;
    private final LinkedList<ViewName> lastViews;
    private final Alert alert;
    private final ManagerAssistant assistant;
    private Account selectedAccount;

    private static ViewsManager instance;

    /**
     * Before calling getInstance, this method must be called
     * @param sessionType The type of the session (BANK or CUSTOMER)
     * @param inputStage The Stage that will contain all the GUI
     */
    public static void init(SessionType sessionType, Stage inputStage) {
        assert sessionType != null;
        stage = inputStage;
        if (instance == null)
            instance = new ViewsManager(sessionType);
    }

    public static ViewsManager getInstance() {
        return instance;
    }

    private ViewsManager(SessionType sessionType) {
        if (sessionType.equals(SessionType.BANK)) {
            session = new EmployeeAppSession(this);
            assistant = new EmployeeManagerAssistant();
        }
        else {
            session = new CustomerAppSession(this);
            assistant = new CustomerManagerAssistant();
        }

        viewsMap = new HashMap<>();
        lastViews = new LinkedList<>();
        baseView = new BaseView(this);

        alert = new ServerUnavailableAlert();
        alert.setOnCloseRequest(event -> closeSession());
    }

    public HashMap<ViewName, InnerView> getViewsMap() {
        return viewsMap;
    }

    public void setPreviousView() {
        lastViews.removeLast();
        ViewName prevViewName = lastViews.pollLast();
        setView(prevViewName);
    }

    public void languageChanged() {
        viewsMap.clear();
        reload();
    }

    private void reload() {
        ViewName viewName = lastViews.pollLast();
        setView(viewName);
    }

    public void setView(ViewName viewName) {
        if (viewsMap.get(viewName) == null)
            viewsMap.put(viewName, assistant.getViewByName(viewName));

        InnerView view = viewsMap.get(viewName);
        baseView.updateContent(view);
        lastViews.addLast(viewName);
    }
    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account account) {
        selectedAccount = account;
    }

    public void alertNoServer() {
        alert.show();
    }

    public BaseView getBaseView() {
        return baseView;
    }

    public String getUserName() {
        return session.isOpen() ? session.getUserName() : I18N.get("disconnected");
    }

    public Session getSession() {
        return session;
    }

    public void userChanged() {
        baseView.updateUsernameLabel();
    }

    public static Stage getStage() {
        return stage;
    }

    public void closeSession() {
        session.close();
        viewsMap.clear();
        lastViews.clear();
        setView(ViewName.LOGIN_VIEW);
        userChanged();
    }
}
