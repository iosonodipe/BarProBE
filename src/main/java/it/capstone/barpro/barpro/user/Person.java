package it.capstone.barpro.barpro.user;

import lombok.Data;

import jakarta.persistence.*;

@MappedSuperclass
@Data
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 40)
    private String firstName;

    @Column(nullable = false, length = 40)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column
    private String profileImage;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private Integer age;

    @Column
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String role; // ADMIN, BARMAN, USER
}
