package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private Long id;
    private RegisteredUserDTO user;
    private String eventDetails;
    private String city;
    private LocalDateTime requestDate;
    private Status status;
}
