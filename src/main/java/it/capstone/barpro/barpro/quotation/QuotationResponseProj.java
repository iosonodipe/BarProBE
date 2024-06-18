package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;

import java.time.LocalDateTime;

public interface QuotationResponseProj {
    User getUser();
    String getEventDetails();
    String getCity();
    LocalDateTime getRequestDate();
}
