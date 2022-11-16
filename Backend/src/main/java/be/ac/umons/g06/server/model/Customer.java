package be.ac.umons.g06.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Customer of a bank
 */
@Entity
@Table(name = "customers")
public class Customer implements User {
    /**
     * ID of the customer
     */
    @Id
    @Column(length = 24)
    @NotBlank
    @Pattern(regexp = "^[^=]*$")
    private String id;
    /**
     * Name of the customer
     */
    @Column(length = 24, nullable = false, unique = true)
    @NotBlank
    private String name;
    /**
     * Password of the customer
     */
    @Column(length = 100, nullable = false)
    @NotBlank
    private String password;
    /**
     * Birthdate of the customer
     */
    @Column(nullable = false)
    @NotNull
    private LocalDate birthdate;

    /**
     * Constructor of Customer
     */
    public Customer() {

    }

    /**
     * Constructor of Customer
     * @param id The ID of the customer
     * @param name The name of the customer
     * @param password The password of the customer
     * @param birthdate The birthdate of the customer
     */
    public Customer(String id, String name, String password, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.birthdate = birthdate;
    }

    /**
     * Get the iID of the customer
     * @return The ID of the customer
     */
    public String getId() {
        return id;
    }

    /**
     * Get the name of the customer
     * @return The name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the customer
     * @param name The name of the customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the password of the customer
     * @return The passwordof the customer
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the customer
     * @param password The password of the customer
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the birthdate of the customer
     * @return The birthdate of the customer
     */
    public LocalDate getBirthdate() {
        return birthdate;
    }

    /**
     * Set the birthdate of the customer
     * @param birthdate The birthdate of the customer
     */
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Get the age of the customer
     * @return The age of the customer
     */
    @JsonIgnore
    public int getAge() {
        Period period = Period.between(birthdate, LocalDate.now());
        return period.getYears();
    }

    /**
     * Checks if the customer is adult
     * @return A boolean object <li>True if the customer is adult</li>
     * <li>False if the customer is not adult</li>
     */
    @JsonIgnore
    public boolean isAdult() {
        return getAge() >= 18;
    }

    /**
     * Get the username of the customer
     * @return The username of the customer
     */
    @Override
    public String getUsername() {
        return "CUSTOMER=" + id;
    }

    /**
     * Get the roles of the customer
     * @return The roles of the customer
     */
    @Override
    public Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleType.ROLE_CUSTOMER));
        return roles;
    }

    /**
     * Checks if the Customer is equals to an Object
     * @param o The object
     * @return A boolean Object <li>True if the object is equals to the customer</li>
     * <li>False if the object is not equals to the customer</li>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Customer other = (Customer) o;
        return id.equals(other.id) && name.equals(other.name) ;
    }

    /**
     * Convert the customer object to a string
     * @return The string
     */
    @Override
    public String toString() {
        return "model.User : { name = " + name + ", nrn = " + id + ", birthdate = " + birthdate + "}";
    }

    /**
     * Hash the ID of the customer
     * @return The ID, the birthdate and the name of the customer hashed
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
