package be.ac.umons.g06.server.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * The signup request
 */
public class SignupRequest {
    /**
     * The ID of the User
     */
    @NotBlank
    @Pattern(regexp = "^[^=]*$")
    @Size(min = 3, max = 15)
    private String id;
    /**
     * The name of the User
     */
    @NotBlank
    @Size(min = 2, max = 12)
    private String name;
    /**
     * The password of the User
     */
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
    /**
     * The Birthdate/Creation date of the User
     */
    @Past
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    /**
     * Get the ID of the User
     * @return The ID of the User
     */
    public String getId() {
        return id;
    }

    /**
     * Get the name of the User
     * @return The name of the User
     */
    public String getName() {
        return name;
    }

    /**
     * Get the password of the User
     * @return The password of the Uspe of LOGINer
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the BirthdateCreation date of the User
     * @return The BirthdateCreation date of the User
     */
    public LocalDate getDate() {
        return date;
    }

}
