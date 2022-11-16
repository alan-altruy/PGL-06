package be.ac.umons.g06.gui;

import be.ac.umons.g06.gui.common.util.I18N;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class I18NTest {

    private static final String c_cedilla = "\u00e7";

    @Test
    void getSupportedLanguagesTest(){
        List<String> lang = new ArrayList<>();
        lang.add("Fran"+c_cedilla+"ais");
        lang.add("English");
        assertEquals(lang, I18N.getSupportedLanguages());
    }

    @Test
    void setLocaleTest(){
        Locale frLoc = new Locale("fr");
        I18N.setLocale(frLoc);
        assertEquals(Locale.getDefault(), frLoc);
        Locale enLoc = new Locale("en");
        I18N.setLocale(enLoc);
        assertEquals(Locale.getDefault(), enLoc);
    }

    @Test
    void getTest(){
        Locale frLoc = new Locale("fr");
        I18N.setLocale(frLoc);
        assertEquals(I18N.get("welcome"), "Bonjour");
        assertEquals(I18N.get("register"), "S'enregistrer");
        Locale enLoc = new Locale("en");
        I18N.setLocale(enLoc);
        assertEquals(I18N.get("welcome"), "Welcome");
        assertEquals(I18N.get("register"), "Sign up");
    }
}
