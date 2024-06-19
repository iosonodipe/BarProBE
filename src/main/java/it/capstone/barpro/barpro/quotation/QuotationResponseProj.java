package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.User;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface QuotationResponseProj {

    Long getId();
    @Value("#{target.user.name}")
    String getNameUser();
    @Value("#{target.user.surname}")
    String getSurnameUser();
    String getEventDetails();
    String getCity();
    LocalDateTime getRequestDate();
}
