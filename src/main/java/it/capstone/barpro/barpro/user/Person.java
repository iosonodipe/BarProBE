package it.capstone.barpro.barpro.user;

import it.capstone.barpro.barpro.role.Role;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@MappedSuperclass
@Data
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 40)
    private String surname;

    @Column
    private String profileImage;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; // ADMIN, BARMAN, USER
}
