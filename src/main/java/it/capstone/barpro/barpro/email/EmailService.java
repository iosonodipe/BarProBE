package it.capstone.barpro.barpro.email;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.booking.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendWelcomeEmail(String recipientEmail) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Benvenuto nella nostra applicazione!");
            helper.setText("Grazie per esserti registrato. Benvenuto!");

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }

    public void sendBookingEmailToBarman(String recipientEmail, Booking booking) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Richiesta di prenotazione");

            // Costruisci il contenuto dell'email come una singola stringa
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hai ricevuto una richiesta di prenotazione, di seguito i dettagli:\n\n");
            emailContent.append(String.format("Cliente: %s %s %s\n", booking.getUser().getFirstName(), booking.getUser().getLastName(), booking.getUser().getEmail()));
            emailContent.append(String.format("Città: %s\n", booking.getCity()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            emailContent.append(String.format("Data: %s\n", booking.getDate().format(formatter)));
            emailContent.append(String.format("Dettagli: %s\n", booking.getEventDetails()));
            emailContent.append("Clicca il link per confermare l'appuntamento: " + "http://localhost:8080/api/bookings/confirm/" + booking.getId() + "\n");

            // Imposta il contenuto dell'email
            helper.setText(emailContent.toString());

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }

    public void sendBookingConfirmationToUser(String recipientEmail, Booking booking) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Prenotazione confermata");

            // Costruisci il contenuto dell'email come una singola stringa
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("La tua richiesta di prenotazione è stata confermata, di seguito i dettagli:\n\n");
            emailContent.append(String.format("Barman: %s %s %s\n", booking.getBarman().getFirstName(), booking.getBarman().getLastName(), booking.getBarman().getEmail()));
            emailContent.append(String.format("Città: %s\n", booking.getCity()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            emailContent.append(String.format("Data: %s\n", booking.getDate().format(formatter)));
            emailContent.append(String.format("Dettagli: %s\n", booking.getEventDetails()));
            emailContent.append("Clicca il link per visualizzare le tue prenotazioni: " + "http://localhost:8080/api/bookings/user/" + booking.getUser().getId() + "\n");

            // Imposta il contenuto dell'email
            helper.setText(emailContent.toString());

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }

    public void sendBookingConfirmationToBarman(String recipientEmail, Booking booking) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Prenotazione confermata");

            // Costruisci il contenuto dell'email come una singola stringa
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hai confermato una prenotazione, di seguito i dettagli:\n\n");
            emailContent.append(String.format("Cliente: %s %s %s\n", booking.getUser().getFirstName(), booking.getUser().getLastName(), booking.getUser().getEmail()));
            emailContent.append(String.format("Città: %s\n", booking.getCity()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            emailContent.append(String.format("Data: %s\n", booking.getDate().format(formatter)));
            emailContent.append(String.format("Dettagli: %s\n", booking.getEventDetails()));
            emailContent.append("Clicca il link per visualizzare le tue prenotazioni: " + "http://localhost:8080/api/bookings/barman/" + booking.getBarman().getId() + "\n");

            // Imposta il contenuto dell'email
            helper.setText(emailContent.toString());

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }

    public void sendResponseEmailToUser(String recipientEmail, Barman barman, Double priceDetails) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Risposta alla tua richiesta di quotazione");

            // Build email content
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("La tua richiesta di quotazione ha ricevuto una risposta, di seguito i dettagli:\n\n");
            emailContent.append(String.format("Barman: %s %s (%s)\n", barman.getFirstName(), barman.getLastName(), barman.getEmail()));
            emailContent.append(String.format("Esperienza: %d anni\n", barman.getExperienceYears()));
            emailContent.append(String.format("Descrizione: %s\n", barman.getDescription()));
            emailContent.append(String.format("Valutazione: %d\n", barman.getRating()));
            emailContent.append(String.format("Prezzo: %s\n", priceDetails));
            emailContent.append("\nClicca il link per visualizzare il profilo del barman: ");
            emailContent.append(String.format("http://localhost:8080/api/barmen/%d\n", barman.getId()));

            // Set the email content
            helper.setText(emailContent.toString());

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // gestisci l'errore in modo adeguato
        }
    }
}
