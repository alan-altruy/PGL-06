package be.ac.umons.g06.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {
    /**
     * New password of the User
     */
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    public String getPassword() {
        return password;
    }
}
