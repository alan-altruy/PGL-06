package be.ac.umons.g06.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BankRegisterTest {

    private BankRegister bankRegister;
    private final Bank bank1 = new Bank("swiftTest1", "nameTest1");
    private final Bank bank2 = new Bank("swiftTest2", "nameTest2");
    private final Bank bank3 = new Bank("swiftTest3", "nameTest3");

    @Test
    void constructor() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        assertEquals(2, bankRegister.getAllBanks().size());
        assertTrue(bankRegister.getAllBanks().contains(bank1));
        assertTrue(bankRegister.getAllBanks().contains(bank2));
    }

    @Test
    void addBank() {
        bankRegister = new BankRegister();
        assertEquals(0, bankRegister.getAllBanks().size());
        assertFalse(bankRegister.getAllBanks().contains(bank1));
        bankRegister.addBank(bank1);
        assertEquals(1, bankRegister.getAllBanks().size());

        bankRegister.addBank(bank2);
        assertTrue(bankRegister.getAllBanks().contains(bank1));
        assertTrue(bankRegister.getAllBanks().contains(bank2));
        assertEquals(2, bankRegister.getAllBanks().size());
    }

    /**
     * Adding twice the same bank should have the same effect as adding once
     */
    @Test
    void addBank2() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        bankRegister.addBank(bank1);
        assertEquals(2, bankRegister.getAllBanks().size());
        bankRegister.addBank(new Bank("swiftTest1", "nameTest1"));
        assertEquals(2, bankRegister.getAllBanks().size());
    }

    @Test
    void getAllBanks() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        bankRegister.addBank(bank3);
        assertEquals(Set.of(bank1, bank2, bank3), bankRegister.getAllBanks());
    }

    @Test
    void getAllBanksNames() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        assertEquals(2, bankRegister.getAllBanksNames().size());
        assertTrue(bankRegister.getAllBanksNames().contains("nameTest1"));
        assertTrue(bankRegister.getAllBanksNames().contains("nameTest2"));
    }

    @Test
    void getBankByName() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        assertEquals(bankRegister.getBankByName("nameTest1").get(), bank1);
        assertEquals(bankRegister.getBankByName("nameTest2").get(), bank2);
        assertTrue(bankRegister.getBankByName("nameTest3").isEmpty());
        assertTrue(bankRegister.getBankByName("swiftTest1").isEmpty());
    }

    @Test
    void getBankBySwift() {
        bankRegister = new BankRegister(List.of(bank1, bank2));
        assertEquals(bankRegister.getBankBySwift("swiftTest1").get(), bank1);
        assertEquals(bankRegister.getBankBySwift("swiftTest2").get(), bank2);
        assertTrue(bankRegister.getBankBySwift("swiftTest3").isEmpty());
        assertTrue(bankRegister.getBankBySwift("nameTest1").isEmpty());
    }
}
