package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "quotations")
@Data
@NoArgsConstructor
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String eventDetails;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDateTime requestDate;

    @Column(nullable = false)
    private Status status; // OPEN, CLOSED
}
