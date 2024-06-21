package it.capstone.barpro.barpro.user.authDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginModel(
        @NotBlank(message = "Lo username  non può contenere solo spazi vuoti")
        @Size(max = 30, message ="Il tuo username è troppo lungo max 30 caratteri")
        String username,
        @NotBlank(message = "La password non può contenere solo spazi vuoti")
        @Size(max = 20, message ="La password è troppo lunga max 20 caratteri")
        String password
) { }