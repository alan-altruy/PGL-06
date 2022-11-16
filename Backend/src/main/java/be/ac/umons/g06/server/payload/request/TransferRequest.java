package be.ac.umons.g06.server.payload.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class TransferRequest {

    @Pattern(regexp = "[A-Z]{2}\\d{14}")
    private String originIban;

    @Pattern(regexp = "[A-Z]{2}\\d{14}")
    private String destinationIban;

    private int amount;

    private String communication;

    @Size(max= 105)
    private String structuredCommunication;

    public String getOriginIban() {
        return originIban;
    }

    public String getDestinationIban() {
        return destinationIban;
    }

    public int getAmount() {
        return amount;
    }

    public String getCommunication() {
        return communication;
    }

    public String getStructuredCommunication() {
        return structuredCommunication;
    }
}
