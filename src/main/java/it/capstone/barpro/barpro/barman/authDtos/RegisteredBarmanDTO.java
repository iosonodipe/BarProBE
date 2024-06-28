package it.capstone.barpro.barpro.barman.authDtos;

import it.capstone.barpro.barpro.roles.Roles;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisteredBarmanDTO {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String city;
    String description;
    Integer experienceYears;
    String avatar;
    Integer rating;
    private List<Roles> roles;

    @Builder(setterPrefix = "with")
    public RegisteredBarmanDTO(Long id, String firstName, String lastName, String username, String email, String city, String description, Integer experienceYears, String avatar, Integer rating, List<Roles> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.city = city;
        this.description = description;
        this.experienceYears = experienceYears;
        this.avatar = avatar;
        this.rating = rating;
        this.roles = roles;
    }
}