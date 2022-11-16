package be.ac.umons.g06.server.payload.response;

import be.ac.umons.g06.server.model.RoleType;
import be.ac.umons.g06.server.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Object that will be serialized to constitute the body of the response to a login request
 */
public class JwtResponse {
    /**
     * The access token
     */
    private final String token;
    /**
     * The User
     */
    private final User user;
    /**
     * The role of the user, used to know if the user is a bank or a customer
     */
    private final RoleType type;

    /**
     * Constructor of JwtResponse
     * @param accessToken The access token
     * @param user The User
     */
    public JwtResponse(@NotEmpty String accessToken, @NotNull User user, @NotNull RoleType type) {
        this.token = accessToken;
        this.user = user;
        this.type = type;
    }

    /**
     * Get the Token of the User
     * @return The token of the User
     */
    public String getToken() {
        return token;
    }

    /**
     * Get the type of the token
     * @return The type of the token
     */
    public RoleType getType() {
        return type;
    }

    /**
     *
     * @return the user if it is a bank, null else
     */
    public User getBank() {
        if (type.equals(RoleType.ROLE_BANK))
            return user;
        return null;
    }

    /**
     *
     * @return the user if it is a customer, null else
     */
    public User getCustomer() {
        if (type.equals(RoleType.ROLE_CUSTOMER))
            return user;
        return null;
    }
}
