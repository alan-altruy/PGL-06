package be.ac.umons.g06.server.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Bank
 */
@Entity
@Table( name = "banks")
public class Bank implements User {
    /**
     * SWIFT of the bank
     */
    @Id
    @Column(length = 20)
    @Pattern(regexp = "^[^=]*$")
    @Size(min = 3, max = 20)
    private String swift;
    /**
     * Name of the bank
     */
    @Column(length = 24, nullable = false, unique = true)
    @Size(min = 3, max = 24)
    private String name;
    /**
     * Password of the bank
     */
    @Column(length = 100, nullable = false)
    private String password;

    /**
     * Empty constructor needed for JPA
     */
    public Bank() {

    }

    /**
     * Constructor of Bank
     * @param swift The SWIFT of the bank
     * @param name The name of the bank
     * @param password The password of the bank
     */
    public Bank(String swift, String name, String password) {
        this.swift = swift;
        this.name = name;
        this.password = password;
    }

    /**
     * Get the SWIFT of the bank
     * @return The SWIFT of the bank
     */
    public String getSwift() {
        return swift;
    }

    /**
     * Get the name of the bank
     * @return The name of the bank
     */
    public String getName() {
        return name;
    }

    /**
     * Get the username of the bank
     * @return The username of the bank
     */
    @Override
    public String getUsername() {
        return "BANK=" + swift;
    }

    /**
     * get the password of the bank
     * @return The password of the bank
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Set the password to a new value
     * @param newPassword the new password
     */
    public void setPassword(String newPassword) {
        password = newPassword;
    }

    /**
     * Get the roles of the bank
     * @return The roles of the bank
     */
    @Override
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_BANK));
        return roles;
    }

    /**
     * Checks if the bank is equals to an Object
     * @param o The object
     * @return A boolean Object <li>True if the Object is equals to the bank</li>
     * <li>False if the Objet is not equals to the bank</li>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Bank other = (Bank) o;
        return name.equals(other.name) && swift.equals(other.swift) ;
    }

    /**
     * Hash the swift and the name of the bank
     * @return The swift and the name of the bank hashed
     */
    @Override
    public int hashCode() {
        return Objects.hash(swift, name);
    }
}
