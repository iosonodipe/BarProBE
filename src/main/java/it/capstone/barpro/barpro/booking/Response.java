package it.capstone.barpro.barpro.booking;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Long id;
    private User user;
    private Barman barman;
    private String eventDetails;
    private String city;
    private LocalDateTime date;
    private Status status;
}
