package it.capstone.barpro.barpro.errors;

public class QuotationAlreadyClosedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "La quotazione risulta già chiusa.";

    public QuotationAlreadyClosedException() {
        super(DEFAULT_MESSAGE);
    }

    public QuotationAlreadyClosedException(String message) {
        super(message);
    }
}
