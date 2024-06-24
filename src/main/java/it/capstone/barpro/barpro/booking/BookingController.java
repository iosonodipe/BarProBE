package it.capstone.barpro.barpro.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponseProj>> getAllBookings(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(bookingService.getAllBookings(page, size, sortBy));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingResponseProj>> getAllUserBookings(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getAllUserBookings(page, size, sortBy, userId));
    }

    @GetMapping("/barman/{barmanId}")
    public ResponseEntity<Page<BookingResponseProj>> getAllBarmanBookings(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @PathVariable Long barmanId) {
        return ResponseEntity.ok(bookingService.getAllBarmanBookings(page, size, sortBy, barmanId));
    }

    @PostMapping
    public ResponseEntity<Response> createBooking(@RequestBody Request request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<String> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateBooking(@PathVariable Long id, @RequestBody Request request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }
}