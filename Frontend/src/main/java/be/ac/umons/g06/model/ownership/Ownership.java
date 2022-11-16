package be.ac.umons.g06.model.ownership;

import be.ac.umons.g06.model.Customer;

import java.util.Collection;

/**
 * This class represents how customers involved in an account can interact with it. The way a customer wan interact with
 * the account is defined by two parameters : 1) his role in the ownership (stored into the OwnershipInvolvement that concerns
 * this customer) and 2) the type of the ownership. If the type is JOIN then a single Customer with the role OWNER can
 * make a decision, but if the type is INDIVIS the same Customer need the validation of the other OWNER customers.
 */
public class Ownership {

    private final OwnershipType type;
    private final Collection<OwnershipInvolvement> involvements;

    /**
     * The constructor is not public to ensure instantiation through OwnershipBuilder
     * @param type The type of the ownership
     * @param involvements The collection of OwnershipInvolvements that concern this ownership
     */
    Ownership(OwnershipType type, Collection<OwnershipInvolvement> involvements) {
        this.type = type;
        this.involvements = involvements;
    }

    public OwnershipType getType() {
        return type;
    }

    public Collection<OwnershipInvolvement> getInvolvements() {
        return involvements;
    }

    public boolean isCustomerInvolved(Customer customer) {
        for (OwnershipInvolvement involvement : involvements) {
            if (customer.equals(involvement.getCustomer()))
                return true;
        }
        return false;
    }
}
