package it.capstone.barpro.barpro.barman.authDtos;

import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDTO {
    RegisteredBarmanDTO barman;
    String token;

    @Builder(setterPrefix = "with")
    public LoginResponseDTO(RegisteredBarmanDTO barman, String token) {
        this.barman = barman;
        this.token = token;
    }
}

