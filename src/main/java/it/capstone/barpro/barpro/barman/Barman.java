package it.capstone.barpro.barpro.barman;

import it.capstone.barpro.barpro.user.Person;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "barmen")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Barman extends Person {
    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false)
    private String description;

    @Column
    private Integer rating;
}