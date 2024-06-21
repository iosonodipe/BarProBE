package it.capstone.barpro.barpro.user.authDtos;

import lombok.Data;

@Data
public class LoginResponseWrapper {
    private LoginResponseDTO userResponse;
    private it.capstone.barpro.barpro.barman.authDtos.LoginResponseDTO barmanResponse;

    public LoginResponseWrapper(LoginResponseDTO userResponse) {
        this.userResponse = userResponse;
    }

    public LoginResponseWrapper(it.capstone.barpro.barpro.barman.authDtos.LoginResponseDTO barmanResponse) {
        this.barmanResponse = barmanResponse;
    }
}