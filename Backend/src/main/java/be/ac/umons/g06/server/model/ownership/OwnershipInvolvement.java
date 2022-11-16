package be.ac.umons.g06.server.model.ownership;

import be.ac.umons.g06.server.model.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Involvement of a Customer in an Ownership. Note that the Involvement in itself has no reference towards the ownership.
 */
@Entity
@Table(name = "involvements")
public class OwnershipInvolvement {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The Customer
     */
    @ManyToOne
    @NotNull
    private Customer customer;

    /**
     * The role of the Customer in the ownership
     */
    @NotNull
    private OwnershipRole role;

    /**
     * True if the Customer has disabled the account concerned by the ownership that has
     */
    @JsonIgnore
    private boolean disabled;

    /**
     * Empty constructor, needed by the JPA.
     */
    public OwnershipInvolvement() {

    }

    /**
     * Two arguments constructor, by default the boolean disabled is false.
     * @param role The role of the customer in the ownership
     * @param customer The customer
     */
    OwnershipInvolvement(OwnershipRole role, Customer customer) {
        this.customer = customer;
        this.role = role;
    }

    public Customer getCustomer() {
        return customer;
    }

    public OwnershipRole getRole() {
        return role;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void switchDisabled() {
        disabled = !disabled;
    }
}
