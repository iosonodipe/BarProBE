package it.capstone.barpro.barpro.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class Request {
    @NotBlank(message = "Il nome non può essere vuoto.")
    private String firstName;
    @NotBlank(message = "Il cognome non può essere vuoto.")
    private String lastName;
    @Email(message = "Inserisci una email valida")
    private String email;
    private String profileImage;
    @NotBlank(message = "La password non può contenere spazi vuoti.")
    @Min(value = 10, message = "La password deve avere almeno 10 caratteri")
    private String password;
    @NotBlank(message = "Il numero di telefono non può essere vuoto.")
    private String phoneNumber;
    @NotNull(message = "La data di nascita non può essere vuota.")
    private LocalDate birthDate;
    private String address;
    @NotEmpty(message = "La città non può essere vuota.")
    private String city;
    @NotBlank(message = "Il CAP deve essere specificato.")
    private String postalCode;
    private String role;
}
