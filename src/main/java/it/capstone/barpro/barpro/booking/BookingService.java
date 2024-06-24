package it.capstone.barpro.barpro.booking;

import it.capstone.barpro.barpro.barman.BarmanRepo;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.email.EmailService;
import it.capstone.barpro.barpro.errors.DateAlreadyBookedException;
import it.capstone.barpro.barpro.user.User;
import it.capstone.barpro.barpro.user.UserRepository;
import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepo bookingRepository;
    private final UserRepository userRepository;
    private final BarmanRepo barmanRepository;
    private final EmailService emailService;


    @Transactional
    public Response createBooking(@Valid Request request) {
        if (!userRepository.existsById(request.getIdUser())) {
            throw new IllegalArgumentException("ID utente non esistente");
        }
        if (!barmanRepository.existsById(request.getIdBarman())) {
            throw new IllegalArgumentException("ID barman non esistente");
        }
        if (isAlreadyBooked(request.getIdUser(), request.getDate())) throw new DateAlreadyBookedException();

        Booking booking = new Booking();
        BeanUtils.copyProperties(request, booking);
        booking.setStatus(Status.PENDING);
        booking.setUser(userRepository.findById(request.getIdUser()).get());
        booking.setBarman(barmanRepository.findById(request.getIdBarman()).get());
        bookingRepository.save(booking);
        emailService.sendBookingEmailToBarman(booking.getBarman().getEmail(), booking);

        Response response = new Response();
        BeanUtils.copyProperties(booking, response);

        RegisteredUserDTO user = new RegisteredUserDTO();
        BeanUtils.copyProperties(booking.getUser(), user);
        response.setUser(user);

        RegisteredBarmanDTO barman = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(booking.getBarman(), barman);
        response.setBarman(barman);

        return response;
    }

    @Transactional
    public String confirmBooking(Long id){
        if (!bookingRepository.existsById(id)){
            throw new EntityNotFoundException("Prenotazione non trovata");
        }
        Booking booking = bookingRepository.findById(id).get();
        booking.setStatus(Status.CONFIRMED);
        bookingRepository.save(booking);
        emailService.sendBookingConfirmationToUser(booking.getUser().getEmail(), booking);
        emailService.sendBookingConfirmationToBarman(booking.getBarman().getEmail(), booking);
        return "Prenotazione confermata";
    }

    public boolean isAlreadyBooked(Long userId, LocalDateTime date) {
        User user = userRepository.findById(userId).get();
        List<BookingResponseProj> bookings = bookingRepository.findAllByUserId(userId);
        for (BookingResponseProj booking : bookings) {
            if (booking.getDate().equals(date) && booking.getStatus() == Status.CONFIRMED) return true;
        }
            return false;
    }

    public Response getBookingById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }
        Booking booking = bookingRepository.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(booking, response);

        RegisteredUserDTO user = new RegisteredUserDTO();
        BeanUtils.copyProperties(booking.getUser(), user);
        response.setUser(user);

        RegisteredBarmanDTO barman = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(booking.getBarman(), barman);
        response.setBarman(barman);

        return response;
    }

    public Page<BookingResponseProj> getAllBookings(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findAllBy(pageable);
    }

    public Page<BookingResponseProj> getAllUserBookings(int page, int size, String sortBy, Long id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findAllByUserId(pageable, id);
    }

    public Page<BookingResponseProj> getAllBarmanBookings(int page, int size, String sortBy, Long id) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return bookingRepository.findAllByBarmanId(pageable, id);
    }

    @Transactional
    public Response updateBooking(Long id, @Valid Request request) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }
        if (!userRepository.existsById(request.getIdUser())) {
            throw new IllegalArgumentException("ID utente non esistente");
        }
        if (!barmanRepository.existsById(request.getIdBarman())) {
            throw new IllegalArgumentException("ID barman non esistente");
        }

        Booking booking = bookingRepository.findById(id).get();
        BeanUtils.copyProperties(request, booking);
        booking.setUser(userRepository.findById(request.getIdUser()).get());
        booking.setBarman(barmanRepository.findById(request.getIdBarman()).get());
        bookingRepository.save(booking);

        Response response = new Response();
        BeanUtils.copyProperties(booking, response);

        RegisteredUserDTO user = new RegisteredUserDTO();
        BeanUtils.copyProperties(booking.getUser(), user);
        response.setUser(user);

        RegisteredBarmanDTO barman = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(booking.getBarman(), barman);
        response.setBarman(barman);

        return response;
    }

    public String deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Prenotazione non trovata.");
        }
        bookingRepository.deleteById(id);
        return "Prenotazione eliminata.";
    }
}