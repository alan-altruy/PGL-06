package be.ac.umons.g06.server.controller;

import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET})
@PreAuthorize("hasRole('BANK') or hasRole('CUSTOMER')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Get a customer from his name or his national registration number
     * @param id The national registration number or the name of the customer
     * @return A response that contains a customer if one is found, and a 404 else.
     */
    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable(name = "id") final String id) {
        if (id == null)
            return ResponseEntity.badRequest().build();

        Optional<Customer> customerById =  customerService.getCustomer(id);
        if (customerById.isPresent())
            return ResponseEntity.ok(customerById.get());

        Optional<Customer> customerByName = customerService.getCustomerByName(id);
        return customerByName.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
