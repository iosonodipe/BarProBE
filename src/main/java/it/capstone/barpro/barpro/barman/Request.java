package it.capstone.barpro.barpro.barman;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @NotEmpty(message = "Il nome non può essere vuoto.")
    private String name;
    @NotEmpty(message = "Il cognome non può essere vuoto.")
    private String surname;
    @Email(message = "Inserisci una email valida")
    private String email;
    private String profileImage;
    @NotBlank(message = "La password non può essere vuota.")
    @Size(min = 10, message = "La password deve avere almeno 10 caratteri.")
    private String password;
    @NotBlank(message = "Il numero di telefono non può essere vuoto.")
    @Size(max = 13, message = "Il numero di telefono non può essere più lungo di 13 caratteri.")
    private String phoneNumber;
    @NotNull(message = "La data di nascita non può essere vuota.")
    private LocalDate birthDate;
    @NotEmpty(message = "La città non può essere vuota.")
    private String city;
    @NotBlank(message = "Il CAP deve essere specificato.")
    @Size(min = 5, max = 5, message = "Il CAP deve essere di 5 cifre.")
    private String postalCode;
    @NotEmpty(message = "La descrizione non può essere vuota.")
    @Size(max = 300, message = "La descrizione può essere di max 300 caratteri")
    private String description;
    @NotNull(message = "Gli anni di esperienza non possono essere vuoti.")
    @Min(value = 1, message = "Gli anni di esperienza devono essere pari o superiori a 1.")
    private Integer experienceYears;
    private Integer rating = 0;
}
