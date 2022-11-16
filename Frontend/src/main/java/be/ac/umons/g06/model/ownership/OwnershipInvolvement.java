package be.ac.umons.g06.model.ownership;

import be.ac.umons.g06.model.Customer;

/**
 * Associates a role and a customer to represent the way a customer is involved into the ownership of an account.
 */
public class OwnershipInvolvement {

    private final OwnershipRole role;
    private final Customer customer;

    public OwnershipInvolvement(OwnershipRole role, Customer customer) {
        this.role = role;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getRole() {
        return role.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OwnershipInvolvement other = (OwnershipInvolvement) o;
        return role.equals(other.role) && customer.equals(other.customer);
    }
}
