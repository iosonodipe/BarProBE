package it.capstone.barpro.barpro.errors;

public class DateAlreadyBookedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "L'utente ha già una prenotazione confermata per la data e l'ora specificate.";

    public DateAlreadyBookedException() {
        super(DEFAULT_MESSAGE);
    }

    public DateAlreadyBookedException(String message) {
        super(message);
    }
}