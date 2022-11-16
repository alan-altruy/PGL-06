package be.ac.umons.g06.model;

public class TransferBuilder {
    private String originIban;
    private int amount;
    private String destinationIban;
    private String communication;
    private String structuredCommunication;

    public TransferBuilder(){}

    public Transfer build() {
        return new Transfer(originIban, destinationIban, amount, communication, structuredCommunication);
    }

    public TransferBuilder originIban(String originIban) {
        this.originIban = originIban;
        return this;
    }

    public TransferBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public TransferBuilder destinationIban(String destinationIban) {
        this.destinationIban = destinationIban;
        return this;
    }

    public TransferBuilder communication(String communication) {
        this.communication = communication;
        return this;
    }

    public TransferBuilder structuredCommunication(String structuredCommunication) {
        this.structuredCommunication = structuredCommunication;
        return this;
    }
}