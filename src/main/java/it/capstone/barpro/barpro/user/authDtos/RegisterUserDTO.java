package it.capstone.barpro.barpro.user.authDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterUserDTO {
    String firstName;
    String lastName;
    String username;
    String email;
    String password;
    String city;
    String avatar;
}