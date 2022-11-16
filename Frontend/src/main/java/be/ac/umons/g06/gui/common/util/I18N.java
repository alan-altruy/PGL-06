package be.ac.umons.g06.gui.common.util;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * This class is used to handle the internationalisation of the App. This method should not be instantiated, all methods
 * are static.
 */
public final class I18N {

    /** the current selected Locale. */
    private static Locale locale;
    private static ResourceBundle bundle;

    static {
        locale = Locale.ENGLISH;
    }

    public static String getLocaleName(Locale loc) {
        String str = loc.getDisplayName(loc);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * get the supported Locales.
     *
     * @return List of Locale objects.
     */
    public static List<Locale> getSupportedLocales() {
        String langStr = ResourceBundle.getBundle("bundle").getString("reserved.languages");
        String[] langArr = langStr.split(":");
        List<Locale> supportedLocales = new ArrayList<>();
        for(String str: langArr) {
            supportedLocales.add(new Locale(str));
        }
        return supportedLocales;
    }

    public static List<String> getSupportedLanguages() {
        List<String> lang = new ArrayList<>();
        for (Locale loc: getSupportedLocales())
            lang.add(getLocaleName(loc));
        return lang;
    }

    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale loc) {
        locale = loc;
        bundle = ResourceBundle.getBundle("bundle", locale);
        Locale.setDefault(loc);
    }

    public static DateTimeFormatter getDateTimePattern(FormatStyle formatStyle) {
        return DateTimeFormatter.ofLocalizedDateTime(formatStyle).withLocale(getLocale());
    }

    public static DateTimeFormatter getDatePattern(FormatStyle formatStyle) {
        return DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(getLocale());
    }

    public static String get(final String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
