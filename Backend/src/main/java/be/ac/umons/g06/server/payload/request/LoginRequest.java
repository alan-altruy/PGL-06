package be.ac.umons.g06.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The login request
 */
public class LoginRequest {
    /**
     * Type of User
     */
    @Pattern(regexp = "CUSTOMER|BANK")
    private String type;
    /**
     * ID of the User
     */
    @NotBlank
    @Pattern(regexp = "^[^=]*$")
    @Size(min = 3, max = 15)
    private String id;
    /**
     * Password of the User
     */
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    /**
     * Get the type of Login
     * @return The type of Login
     */
    public String getType() {
        return type;
    }

    /**
     * Get the ID of the User
     * @return The ID of the User
     */
    public String getId() {
        return id;
    }

    /**
     * Get the password of the User
     * @return The password of the User
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the username of the User
     * @return The username of the User
     */
    public String getUsername() {
        return type + "=" + id;
    }
}
