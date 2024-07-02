package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface QuotationResponseProj {

    Long getId();
    @Value("#{target.user.firstName}")
    String getNameUser();
    @Value("#{target.user.lastName}")
    String getSurnameUser();
    String getEventDetails();
    String getCity();
    LocalDateTime getRequestDate();
    String getStatus();
}
