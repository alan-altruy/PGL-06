package be.ac.umons.g06.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * Interface of a User
 */
public interface User {
    /**
     * Get the username of the User
     * @return The username of the User
     */
    @JsonIgnore
    String getUsername();

    /**
     * Get the password of the User
     * @return The password of the User
     */
    @JsonIgnore
    String getPassword();

    /**
     * Get the roles of the User
     * @return The roles of the User
     */
    @JsonIgnore
    Set<Role> getRoles();

    /**
     * Get one role of the User
     * @return one of the User's roles
     */
    @JsonIgnore
    default RoleType getRole() {
        for (Role role : getRoles())
            return role.getName();
        return null;
    }
}
