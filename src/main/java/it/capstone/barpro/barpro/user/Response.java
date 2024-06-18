package it.capstone.barpro.barpro.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
