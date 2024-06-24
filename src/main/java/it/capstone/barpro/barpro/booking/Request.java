package it.capstone.barpro.barpro.booking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @NotNull
    private Long idUser;

    @NotNull
    private Long idBarman;

    @NotNull(message = "Devi specificare la data")
    private LocalDateTime date;

    @NotEmpty(message = "Devi necessariamente aggiungere i dettagli dell'evento")
    @Size(min = 100, max = 300, message = "Minimo 100 caratteri, Massimo 300 caratteri")
    private String eventDetails;

    @NotEmpty(message = "Inserisci la città dell'evento")
    private String city;

}
