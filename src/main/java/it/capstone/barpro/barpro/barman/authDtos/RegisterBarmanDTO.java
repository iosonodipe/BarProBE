package it.capstone.barpro.barpro.barman.authDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterBarmanDTO {
    String firstName;
    String lastName;
    String username;
    String email;
    String password;
    String city;
    String avatar;
    Integer experienceYears;
    String description;
    Integer rating = 0;
}