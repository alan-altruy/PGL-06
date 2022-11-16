package be.ac.umons.g06.server.payload.response;

/**
 * Message Response
 */
public class MessageResponse {
    /**
     * The message
     */
    private String message;

    /**
     * Constructor of MessageResponse
     * @param message The message
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * Get the message of the Response
     * @return The message of the Reponse
     */
    public String getMessage() {
        return message;
    }

    /**
     *  Set the message
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
