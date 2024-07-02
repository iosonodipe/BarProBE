package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.barman.Barman;
import it.capstone.barpro.barpro.barman.BarmanRepo;
import it.capstone.barpro.barpro.booking.Booking;
import it.capstone.barpro.barpro.booking.BookingRepo;
import it.capstone.barpro.barpro.booking.BookingService;
import it.capstone.barpro.barpro.email.EmailService;
import it.capstone.barpro.barpro.errors.DateAlreadyBookedException;
import it.capstone.barpro.barpro.errors.QuotationAlreadyClosedException;
import it.capstone.barpro.barpro.user.User;
import it.capstone.barpro.barpro.user.UserRepository;
import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Service
@Validated
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepo repo;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final BarmanRepo barmanRepository;
    private final BookingRepo bookingRepository;
    private final BookingService bookingService;

    public Page<QuotationResponseProj> findAll(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAllBy(pageable);
    }

    public Page<QuotationResponseProj> findAllByCity(int page, int size, String city){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllByCity(pageable, city);
    }

    public Page<QuotationResponseProj> findAllByUser(int page, int size, String sortBy, Long id){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAllByUserId(pageable, id);
    }


    public Response findById(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        Quotation quotation = repo.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);
        return response;
    }
    @Transactional
    public Response create(@Valid Request request){
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(request, quotation);
        quotation.setStatus(Status.OPEN);
        quotation.setUser(userRepository.findById(request.getIdUser()).get());
        repo.save(quotation);

        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);

        RegisteredUserDTO user = new RegisteredUserDTO();
        BeanUtils.copyProperties(quotation.getUser(), user);
        response.setUser(user);

        return response;
    }

    @Transactional
    public void respondToQuotation(Long quotationId, Long barmanId, Double price){
         if (!repo.existsById(quotationId)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
         if (!userRepository.existsById(barmanId)){
            throw new EntityNotFoundException("Barman non trovato.");
        }
         if(repo.findById(quotationId).get().getStatus() != Status.CLOSED){
            Barman barman = barmanRepository.findById(barmanId).get();
             Long userId = repo.findById(quotationId).get().getUser().getId();
             User user = userRepository.findById(userId).get();
            emailService.sendResponseEmailToUser(user.getEmail(), barman, price);
         } else throw new QuotationAlreadyClosedException();
    }

    @Transactional
    public String acceptQuotation(Long quotationId, Long barmanId) {
        if(repo.findById(quotationId).get().getStatus() == Status.CLOSED) throw new QuotationAlreadyClosedException();
        Quotation quotation = repo.findById(quotationId).orElseThrow(() -> new EntityNotFoundException("Richiesta di quotazione non trovata."));
        Barman barman = barmanRepository.findById(barmanId).orElseThrow(() -> new EntityNotFoundException("Barman non trovato."));

        if (bookingService.isUserAlreadyBooked(quotation.getUser().getId(), quotation.getRequestDate())) throw new DateAlreadyBookedException();
        else if(bookingService.isUserAlreadyBooked(quotation.getUser().getId(), quotation.getRequestDate())) throw new DateAlreadyBookedException("Il barman richiesto ha già una prenotazione confermata per il giorno selezionato.");
        else{
            Booking booking = new Booking();
            BeanUtils.copyProperties(quotation, booking, "id", "status", "requestDate"); // Copia le proprietà da Quotation a Booking
            booking.setDate(quotation.getRequestDate());
            booking.setStatus(it.capstone.barpro.barpro.booking.Status.PENDING);
            booking.setUser(quotation.getUser());
            booking.setBarman(barman);
            bookingRepository.save(booking);

            bookingService.confirmBooking(booking.getId());
            // Chiudere la quotazione
            quotation.setStatus(Status.CLOSED);
            repo.save(quotation);
        }
        return "Quotazione confermata";
    }


    @Transactional
    public String delete(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        repo.deleteById(id);
        return "Richiesta di quotazione eliminata.";
    }

    @Transactional
    public Response update(Long id, @Valid Request request){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        Quotation quotation = repo.findById(id).get();
        BeanUtils.copyProperties(request, quotation);
        repo.save(quotation);

        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);

        RegisteredUserDTO user = new RegisteredUserDTO();
        BeanUtils.copyProperties(quotation.getUser(), user);
        response.setUser(user);

        return response;
    }
}
