package be.ac.umons.g06.model;

public class Transfer {

    private final String originIban;
    private final String destinationIban;
    private final int amount;
    private final String communication;
    private final String structuredCommunication;

    public Transfer(String originIban, String destinationIban, int amount, String communication, String structuredCommunication) {
        this.originIban = originIban;
        this.amount = amount;
        this.destinationIban = destinationIban;
        this.communication = communication;
        this.structuredCommunication = structuredCommunication;
    }
}