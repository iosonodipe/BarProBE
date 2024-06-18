package it.capstone.barpro.barpro.booking;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Booking {
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
    private LocalDateTime date;

    @Column(nullable = false)
    private Status status; // PENDING, CONFIRMED, COMPLETED, CANCELLED

    @Column(nullable = false)
    private String eventDetails;

    @Column(nullable = false)
    private String city;
}
