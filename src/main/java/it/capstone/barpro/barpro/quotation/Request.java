package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Request {
    @NotEmpty
    private Long idUser;

    @NotEmpty
    private String eventDetails;

    @NotEmpty
    private String city;

    @NotNull
    private LocalDateTime requestDate;
}
