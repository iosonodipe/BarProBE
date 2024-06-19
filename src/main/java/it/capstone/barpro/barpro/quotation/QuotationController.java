package it.capstone.barpro.barpro.quotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Request request) { return ResponseEntity.ok(svc.create(request)); }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody Request request) { return ResponseEntity.ok(svc.update(id, request)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) { return ResponseEntity.ok(svc.delete(id)); }
}
