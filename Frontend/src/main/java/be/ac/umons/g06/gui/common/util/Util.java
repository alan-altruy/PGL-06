package be.ac.umons.g06.gui.common.util;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import be.ac.umons.g06.model.account.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Only static methods and constants
 */
public class Util {
    public static final String EURO = "\u20ac";

    public static Label getUnderlinedLabel(String text) {
        Label label = new Label(text);
        label.setUnderline(true);
        return label;
    }

    public static String getAccountIbanFromDescription(String description) {
        return description.replace(" ", "").split("-")[1];
    }

    public static String getAccountDescription(Account account) {
        return I18N.get(account.getType().toString()) + " - " + account.prettyPrintIban();
    }

    public static String getLongAccountDescription(Account account) {
        return account.getBank().getName() + " - " + I18N.get(account.getType().toString()) + " - " + account.prettyPrintIban();
    }

    public static Collection<String> getAccountsDescriptions(Collection<? extends Account> accounts) {
        Collection<String> descriptions = new ArrayList<>();
        for (Account account: accounts)
            descriptions.add(getAccountDescription(account));
        return descriptions;
    }

    public static Collection<String> getLongAccountsDescriptions(Collection<? extends Account> accounts) {
        Collection<String> descriptions = new ArrayList<>();
        for (Account account: accounts)
            descriptions.add(getLongAccountDescription(account));
        return descriptions;
    }

    /**
     * Takes a string where some parts are surrounded by brackets. Return a string where the parts in brackets are
     * translated (using I18N.get method), and the other parts remains unmodified.
     *
     * For example, the string  "{transfer} {from} BE1418 {to} BE3945." will be converted (if the chosen Locale is
     * french) into "Virement depuis BE1418 vers BE3945.
     *
     * @param parametricDescription The string that we want to partially translate.
     * @return The string with the specified parts translated
     */
    public static String convertDescription(String parametricDescription) {
        String patternString = "\\{[^{}]*}";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(parametricDescription);

        int start = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            if (matcher.start() > start)
                stringBuilder.append(parametricDescription, start, matcher.start());
            stringBuilder.append(I18N.get(parametricDescription.substring(matcher.start() + 1, matcher.end() - 1)));
            start = matcher.end();
        }
        stringBuilder.append(I18N.get(parametricDescription.substring(start)));
        return stringBuilder.toString();
    }

    public static String reviseIban(String currentText, String addedText) {
        String iban = currentText.replaceAll(" ","") + addedText.replaceAll(" ", "");
        boolean ok = false;
        if (iban.length() < 2)
            ok = iban.matches("[A-Z]");
        else if (iban.length() < 3)
            ok = iban.matches("[A-Z]{2}");
        else if (iban.length() < 17) {
            iban += "4".repeat(16 - iban.length());
            ok = iban.matches("[A-Z]{2}\\d{14}");
        }
        return ok ? addedText : "";
    }

    public static Separator getCustomSeparator() {
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.getStyleClass().add("parameters-separator");
        return separator;
    }
}
