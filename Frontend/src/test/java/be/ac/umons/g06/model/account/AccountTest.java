package be.ac.umons.g06.model.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void prettyPrintIbanTest() {
        assertEquals(Account.prettyPrintIban("BE01234567891234"), "BE01 2345 6789 1234");
        assertEquals(Account.prettyPrintIban("BE45678912652312"), "BE45 6789 1265 2312");
    }

    @Test
    void isValidIbanTest() {
        assertTrue(Account.isValidIban("BE68 5390 0754 7034"));
        assertTrue(Account.isValidIban("BE68539007547034"));
        assertFalse(Account.isValidIban("BE68539007   034"));
        assertFalse(Account.isValidIban("BE01234567   234"));
        assertFalse(Account.isValidIban("BE01234567891234"));
        assertFalse(Account.isValidIban("BE012 345 6789 1234"));
    }
}