package it.capstone.barpro.barpro.quotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    @Autowired
    QuotationService svc;

    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id) { return ResponseEntity.ok(svc.findById(id)); }

    @GetMapping
    public ResponseEntity<Page<QuotationResponseProj>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy){
        return ResponseEntity.ok(svc.findAll(page, size, sortBy));
    }
    @GetMapping("/byCity")
    public ResponseEntity<Page<QuotationResponseProj>> findAllByCity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String city){
        return ResponseEntity.ok(svc.findAllByCity(page, size, city));
    }

    @GetMapping("/byUser/{id}")
    public ResponseEntity<Page<QuotationResponseProj>> findAllByUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "requestDate") String sortBy, @PathVariable Long id){
        return ResponseEntity.ok(svc.findAllByUser(page, size, sortBy, id));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Request request) { return ResponseEntity.ok(svc.create(request)); }

    @PostMapping("/{id}/respond")
    public ResponseEntity<Void> respondToQuotation(@PathVariable Long id, @RequestParam Long barmanId, @RequestParam Double priceDetails) {
        svc.respondToQuotation(id, barmanId, priceDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptQuotation(@PathVariable Long id, @RequestParam Long idBarman) {
        return ResponseEntity.ok(svc.acceptQuotation(id, idBarman));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody Request request) { return ResponseEntity.ok(svc.update(id, request)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) { return ResponseEntity.ok(svc.delete(id)); }
}
