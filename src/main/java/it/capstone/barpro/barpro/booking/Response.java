package it.capstone.barpro.barpro.booking;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.user.User;
import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Long id;
    private RegisteredUserDTO user;
    private RegisteredBarmanDTO barman;
    private String eventDetails;
    private String city;
    private LocalDateTime date;
    private Status status;
}
