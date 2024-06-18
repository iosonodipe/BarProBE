package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private Long id;
    private User user;
    private String eventDetails;
    private String city;
    private LocalDateTime requestDate;
}
