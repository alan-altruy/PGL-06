package be.ac.umons.g06.server.model.ownership;

import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The ownership of an account
 */
@Entity
@Table(name = "ownership")
public class Ownership {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Account account;

    @Column(nullable = false)
    private OwnershipType type;
    /**
     * List of ownership involvement
     */
    @OneToMany(cascade = CascadeType.ALL)
    @NotEmpty
    private List<OwnershipInvolvement> involvements;

    /**
     * Ownership constructor
     */
    public Ownership() {

    }

    /**
     * Ownership constructor
     * @param type Type of the ownership
     * @param involvementSet Set of involvements
     */
    public Ownership(@NotNull OwnershipType type, @NotEmpty Set<OwnershipInvolvement> involvementSet) {
        this.type = type;
        this.involvements = new ArrayList<>(involvementSet);
    }

    /**
     * Get the ID of the ownership
     * @return The ID of the ownership
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * Get the account of the ownership
     * @return The account of the ownership
     */
    @JsonIgnore
    @NotNull
    public Account getAccount() {
        return account;
    }

    /**
     * Set the account of the ownership
     * @param account The account of the ownership
     */
    public void setAccount(@NotNull Account account) {
        this.account = account;
    }

    /**
     * Get the type of the ownership
     * @return The type of the ownership
     */
    @NotNull
    public OwnershipType getType() {
        return type;
    }

    /**
     * Checks if the customer is disabled
     * @param customer The customer
     * @return A boolean object <li>True if the customer is disabled</li><li>False if the customer is enabled</li>
     */
    public boolean isCustomerDisabled(Customer customer) {
        for (OwnershipInvolvement involvement: involvements) {
            if (involvement.getCustomer().equals(customer))
                return involvement.isDisabled();
        }
        return true;
    }

    /**
     * Checks if the customer is involved
     * @param customer The customer
     * @return A boolean object <li>True if the customer is involved</li><li>False if the customer is not involved</li>
     */
    public boolean isCustomerInvolved(Customer customer) {
        for (OwnershipInvolvement involvement: involvements) {
            if (involvement.getCustomer().equals(customer))
                return true;
        }
        return false;
    }

    /**
     * Get the owners of the ownership
     * @return The list of the owners
     */
    @JsonIgnore
    public List<Customer> getOwners() {
        return involvements.stream().map(OwnershipInvolvement::getCustomer).collect(Collectors.toList());
    }

    /**
     * Get the involvements of the ownership
     * @return The involvements of th ownership
     */
    @NotEmpty
    public List<OwnershipInvolvement> getInvolvements() {
        return involvements;
    }

    /**
     * Check if the ownership is valid
     * @return A boolean object <li>True if the ownership is valid</li><li>False if the ownership is not valid</li>
     */
    @JsonIgnore
    @AssertTrue
    public boolean isValid() {
        switch (type) {
            case INDIVIDUAL:
                return involvements.size() == 1 && involvements.get(0).getRole().equals(OwnershipRole.ROLE_OWNER);
            case YOUNG:
                if (involvements.size() == 2) {
                    boolean supervisorFound = false;
                    boolean youngFound = false;
                    if (isValidSupervisor(involvements.get(0)) || isValidSupervisor(involvements.get(1)))
                        supervisorFound = true;
                    if (isValidYoung(involvements.get(0)) || isValidYoung(involvements.get(1)))
                        youngFound = true;
                    return youngFound && supervisorFound;
                }
                return false;
            case JOIN:
            case INDIVIS:
                if (involvements.size() > 1) {
                    for (OwnershipInvolvement involvement : involvements) {
                        if (!involvement.getRole().equals(OwnershipRole.ROLE_OWNER))
                            return false;
                    }
                    return true;
                }
                return false;
        }
        return false;
    }

    /**
     * Check if the involvement is a supervisor of the ownership
     * @param involvement The involvement
     * @return A boolean object <li>True if the involvement is a supervisor of the ownership</li
     * <li>False if the involvement is not a supervisor of the ownership</li>
     */
    private static boolean isValidSupervisor(OwnershipInvolvement involvement) {
        return involvement.getRole().equals(OwnershipRole.ROLE_SUPERVISOR) && involvement.getCustomer().isAdult();
    }
    /**
     * Check if the involvement is a young of the ownership
     * @param involvement The involvement
     * @return A boolean object <li>True if the involvement is a young of the ownership</li
     * <li>False if the involvement is not a young of the ownership</li>
     */
    private static boolean isValidYoung(OwnershipInvolvement involvement) {
        return involvement.getRole().equals(OwnershipRole.ROLE_YOUNG) && !involvement.getCustomer().isAdult();
    }

    /**
     * Modifies the disabled boolean in the OwnershipInvolvement that makes the link between the given Customer and this
     * Ownership. The boolean becomes true if it was false and false if it was true.
     * @param customer The customer who will deactivate the account related to this ownership
     */
    public void switchCustomerDisabled(Customer customer) {
        for (OwnershipInvolvement involvement : involvements)
            if (involvement.getCustomer().equals(customer))
                involvement.switchDisabled();
    }
}
