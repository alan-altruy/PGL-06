package be.ac.umons.g06.server.model.ownership;

import be.ac.umons.g06.server.model.Customer;

import javax.validation.constraints.NotNull;
import java.util.*;

public class OwnershipBuilder {

    /**
     * Type of the ownership
     */
    private OwnershipType type;

    /**
     * The Map that stores all the OwnershipInvolvements that concern the Ownership that will be build through this
     * object. A Map with Customer as keys is used to ensure that a customer is never involved more than once in the
     * ownership.
     */
    private final Map<Customer, OwnershipInvolvement> involvedUsers;

    /**
     * Constructor of OwnershipBuilder
     */
    public OwnershipBuilder() {
        involvedUsers = new HashMap<>();
    }

    /**
     * Build the Ownership
     * @return The Ownership
     */
    public Ownership build() {
        assert type != null && involvedUsers.size() > 0;
        return new Ownership(type, new HashSet<>(involvedUsers.values()));
    }

    /**
     * Set the type of the ownership
     * @param type The type of the ownership
     * @return OwnershipBuilder
     */
    public OwnershipBuilder type(@NotNull OwnershipType type) {
        this.type = type;
        return this;
    }

    /**
     * Set the owner of the Ownership
     * @param owner The owner of the Ownership
     * @return OwnershipBuilder
     */
    public OwnershipBuilder owner(@NotNull Customer owner) {
        involvedUsers.put(owner, new OwnershipInvolvement(OwnershipRole.ROLE_OWNER, owner));
        return this;
    }

    /**
     * Set the supervisor of the Ownership
     * @param supervisor The customer who is over 18 years old
     * @return OwnershipBuilder
     */
    public OwnershipBuilder supervisor(@NotNull Customer supervisor) {
        assert supervisor.isAdult();
        involvedUsers.put(supervisor, new OwnershipInvolvement(OwnershipRole.ROLE_SUPERVISOR, supervisor));
        return this;
    }

    /**
     * Set the young of the Ownership
     * @param young A Customer who is under the age of 18
     * @return OwnershipBuilder
     */
    public OwnershipBuilder young(@NotNull Customer young) {
        assert !young.isAdult();
        involvedUsers.put(young, new OwnershipInvolvement(OwnershipRole.ROLE_YOUNG, young));
        return this;
    }
}