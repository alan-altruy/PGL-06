package be.ac.umons.g06.model;

public class RestResponse<T> {

    private final T payload;
    private final String message;
    private final boolean success;

    public RestResponse(T payload, boolean success, String message) {
        this.payload = payload;
        this.success = success;
        this.message = message;
    }

    public RestResponse(T payload) {
        this(payload, true, "");
    }

    public RestResponse(T payload, boolean success) {
        this(payload, success, "");
    }

    public T getPayload() {
        return payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}