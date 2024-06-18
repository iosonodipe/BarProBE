package it.capstone.barpro.barpro.review;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.user.User;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "barman_id", nullable = false)
    private Barman barman;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 300)
    private String comment;
}