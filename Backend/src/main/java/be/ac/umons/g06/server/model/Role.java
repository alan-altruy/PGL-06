package be.ac.umons.g06.server.model;

/**
 * Role
 */
public class Role {
    /**
     * Name of the role
     */
    private RoleType name;

    /**
     * Constructor of Role
     */
    public Role() {

    }

    /**
     * Constructor of role
     * @param name The name of the role
     */
    public Role(RoleType name) {
        this.name = name;
    }

    /**
     * Get the name of the role
     * @return The name of the role
     */
    public RoleType getName() {
        return name;
    }

    /**
     * Set the name of the role
     * @param name The name of the role
     */
    public void setName(RoleType name) {
        this.name = name;
    }

}
