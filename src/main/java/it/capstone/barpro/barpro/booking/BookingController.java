package it.capstone.barpro.barpro.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Ottieni una prenotazione specifica per ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazione trovata"),
            @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> getBookingById(
            @Parameter(description = "ID della prenotazione") @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @Operation(summary = "Ottieni tutte le prenotazioni")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazioni trovate"),
            @ApiResponse(responseCode = "404", description = "Nessuna prenotazione trovata")
    })
    @GetMapping
    public ResponseEntity<Page<BookingResponseProj>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(bookingService.getAllBookings(page, size, sortBy));
    }

    @Operation(summary = "Ottieni tutte le prenotazioni di un utente specifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazioni trovate"),
            @ApiResponse(responseCode = "404", description = "Nessuna prenotazione trovata")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingResponseProj>> getAllUserBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "ID dell'utente") @PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getAllUserBookings(page, size, sortBy, userId));
    }

    @Operation(summary = "Ottieni tutte le prenotazioni di un barman specifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazioni trovate"),
            @ApiResponse(responseCode = "404", description = "Nessuna prenotazione trovata")
    })
    @GetMapping("/barman/{barmanId}")
    public ResponseEntity<Page<BookingResponseProj>> getAllBarmanBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "ID del barman") @PathVariable Long barmanId) {
        return ResponseEntity.ok(bookingService.getAllBarmanBookings(page, size, sortBy, barmanId));
    }

    @Operation(summary = "Crea una nuova prenotazione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazione creata con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida")
    })
    @PostMapping
    public ResponseEntity<Response> createBooking(
            @Parameter(description = "Dettagli della richiesta di prenotazione") @RequestBody Request request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @Operation(summary = "Conferma una prenotazione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazione confermata con successo"),
            @ApiResponse(responseCode = "401", description = "Errore nella conferma della prenotazione")
    })
    @PatchMapping(value = "/{id}/confirm", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> confirmBooking(
            @Parameter(description = "ID della prenotazione") @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @Operation(summary = "Modifica una prenotazione esistente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazione modificata con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida"),
            @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateBooking(
            @Parameter(description = "ID della prenotazione") @PathVariable Long id,
            @Parameter(description = "Dettagli della modifica della prenotazione") @RequestBody Request request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @Operation(summary = "Elimina una prenotazione")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prenotazione eliminata con successo"),
            @ApiResponse(responseCode = "404", description = "Prenotazione non trovata")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @Parameter(description = "ID della prenotazione") @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }
}