package it.capstone.barpro.barpro.barman.authDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterBarmanModel(

        @NotBlank(message = "Il tuo nome non può essere vuoto")
        @Size(max = 40, message ="Il tuo nome è troppo lungo max 40 caratteri")
        String firstName,
        @NotBlank(message = "Il tuo cognome non può essere vuoto")
        @Size(max = 40, message ="Il tuo cognome è troppo lungo max 40 caratteri")
        String lastName,
        @NotBlank(message = "Lo username  non può contenere solo spazi vuoti")
        @Size(max = 30, message ="Il tuo username è troppo lungo max 30 caratteri")
        String username,
        @Email(message = "Inserisci una email valida")
        String email,
        @NotBlank(message = "La password non può contenere solo spazi vuoti")
        @Size(max = 20, message ="La password è troppo lunga max 20 caratteri")
        String password,
        @NotBlank(message = "La città non può essere vuota.")
        String city,
        String avatar,
        @NotNull(message = "Devi inserire gli anni di esperienza")
        Integer experienceYears,
        @NotBlank(message = "La descrizione non può essere vuota.")
        String description
) {
}