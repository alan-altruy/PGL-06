package be.ac.umons.g06.model.ownership;

import be.ac.umons.g06.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class OwnershipBuilder {

    private OwnershipType type;
    private final Map<Customer, OwnershipInvolvement> involvedUsers;

    public OwnershipBuilder() {
        involvedUsers = new HashMap<>();
    }

    public Ownership build() {
        assert type != null;
        switch (type) {
            case JOIN:
            case INDIVIS:
                return new Ownership(type, involvedUsers.values());
            case YOUNG:
                assert involvedUsers.size() == 2;
                return new Ownership(type, involvedUsers.values());
            default:
                assert involvedUsers.size() == 1;
                return new Ownership(OwnershipType.INDIVIDUAL, involvedUsers.values());
        }
    }

    public OwnershipBuilder type(OwnershipType type) {
        assert type != null;
        this.type = type;
        return this;
    }

    public OwnershipBuilder involvement(OwnershipRole role, Customer customer) {
        assert role != null && customer != null;
        involvedUsers.put(customer, new OwnershipInvolvement(role, customer));
        return this;
    }

    public OwnershipBuilder involvement(OwnershipInvolvement involvement) {
        assert involvement != null && involvement.getRole() != null && involvement.getCustomer() != null;
        involvedUsers.put(involvement.getCustomer(), involvement);
        return this;
    }

    public OwnershipBuilder owner(Customer owner) {
        assert owner != null;
        involvedUsers.put(owner, new OwnershipInvolvement(OwnershipRole.ROLE_OWNER, owner));
        return this;
    }

    public OwnershipBuilder supervisor(Customer supervisor) {
        assert supervisor != null && supervisor.isAdult();
        involvedUsers.put(supervisor, new OwnershipInvolvement(OwnershipRole.ROLE_SUPERVISOR, supervisor));
        return this;
    }

    public OwnershipBuilder young(Customer young) {
        assert young != null && !young.isAdult();
        involvedUsers.put(young, new OwnershipInvolvement(OwnershipRole.ROLE_YOUNG, young));
        return this;
    }
}
