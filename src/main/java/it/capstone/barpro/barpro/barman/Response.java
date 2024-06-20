package it.capstone.barpro.barpro.barman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String city;
    private String description;
    private Integer experienceYears;
    private Integer rating;
}
