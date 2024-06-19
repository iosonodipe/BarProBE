package it.capstone.barpro.barpro.booking;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.user.User;

import java.time.LocalDateTime;

public interface BookingResponseProj {
    User getUser();
    Barman getBarman();
    String getEventDetails();
    String getCity();
    Status getStatus();
    LocalDateTime getDate();
}
