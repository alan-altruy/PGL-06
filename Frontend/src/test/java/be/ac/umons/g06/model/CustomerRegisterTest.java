package be.ac.umons.g06.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegisterTest {

    @Test
    void findCustomer() {
        CustomerRegister customerRegister = new CustomerRegister();
        Customer customer1 = new Customer("nameTest1", "nrnTest1",
                LocalDate.of(2001, 5, 17));
        Customer customer2 = new Customer("nameTest2", "nrnTest2",
                LocalDate.of(2002, 4, 27));
        Customer customer3 = new Customer("nameTest3", "nrnTest3",
                LocalDate.of(2003, 1, 14));
        customerRegister.addCustomer(customer1);
        customerRegister.addCustomer(customer2);
        customerRegister.addCustomer(customer3);
        assertTrue(customerRegister.findCustomer("nrnTest1").isPresent());
        assertTrue(customerRegister.findCustomer("nrnTest2").isPresent());
        assertTrue(customerRegister.findCustomer("nrnTest3").isPresent());
        assertTrue(customerRegister.findCustomer("nameTest1").isPresent());
        assertTrue(customerRegister.findCustomer("nameTest2").isPresent());
        assertTrue(customerRegister.findCustomer("nameTest3").isPresent());
        assertTrue(customerRegister.findCustomer("nrnTest4").isEmpty());
        assertTrue(customerRegister.findCustomer("nameTest4").isEmpty());
    }
}
