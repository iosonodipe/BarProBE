package it.capstone.barpro.barpro.booking;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Request {
    @NotNull
    private Long idUser;

    @NotNull
    private Long idBarman;

    @NotNull
    private LocalDateTime date;

    @NotEmpty
    private String eventDetails;

    @NotEmpty
    private String city;

}
