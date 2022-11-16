package be.ac.umons.g06.model.ownership;

import be.ac.umons.g06.model.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class OwnershipTest {

    @Test
    void ownershipTest(){
        Customer customer1 = new Customer("nameTest1", "nrnTest1",
                LocalDate.of(1999, 11, 6));
        Customer customer2 = new Customer("nameTest2", "nrnTest2",
                LocalDate.of(1998, 12, 1));
        OwnershipInvolvement adultOwnershipInvolvement1 = new OwnershipInvolvement(OwnershipRole.ROLE_OWNER, customer1);
        OwnershipInvolvement adultOwnershipInvolvement2 = new OwnershipInvolvement(OwnershipRole.ROLE_OWNER, customer2);
        Ownership ownership1 = new OwnershipBuilder()
                .type(OwnershipType.INDIVIS)
                .involvement(adultOwnershipInvolvement1)
                .involvement(adultOwnershipInvolvement2)
                .build();
        assertEquals(ownership1.getType(), OwnershipType.INDIVIS);
        Collection<OwnershipInvolvement> involvements = new ArrayList<>();
        involvements.add(adultOwnershipInvolvement1);
        involvements.add(adultOwnershipInvolvement2);
        assertTrue(ownership1.getInvolvements().containsAll(involvements));
        assertTrue(involvements.containsAll(ownership1.getInvolvements()));

        Customer youngCustomer = new Customer("nameTest3", "nrnTest3",
                LocalDate.of(2018, 3, 14));
        OwnershipInvolvement youngOwnershipInvolvement = new OwnershipInvolvement(OwnershipRole.ROLE_YOUNG, youngCustomer);
        Ownership ownership2 = new OwnershipBuilder()
                .type(OwnershipType.YOUNG)
                .involvement(adultOwnershipInvolvement1)
                .involvement(OwnershipRole.ROLE_YOUNG, youngCustomer)
                .build();
        assertEquals(ownership2.getType(), OwnershipType.YOUNG);
        assertFalse(youngOwnershipInvolvement.getCustomer().isAdult());
        assertTrue(adultOwnershipInvolvement1.getCustomer().isAdult());
    }
}