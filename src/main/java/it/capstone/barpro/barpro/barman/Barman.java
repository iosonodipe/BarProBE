package it.capstone.barpro.barpro.barman;

import it.capstone.barpro.barpro.user.User;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "barmen")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barman extends User {
    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false)
    private String description;

    @Column
    private Integer rating;

}
