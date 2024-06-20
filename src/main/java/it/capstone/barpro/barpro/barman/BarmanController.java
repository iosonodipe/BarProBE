package it.capstone.barpro.barpro.barman;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barmen")
public class BarmanController {

    @Autowired
    private BarmanService svc;

    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BarmanResponseProj>> findAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(svc.findAll(page, size, sortBy));
    }

    @GetMapping("/byCity")
    public ResponseEntity<Page<BarmanResponseProj>> findAllByCity(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam String city) {
        return ResponseEntity.ok(svc.findAllByCity(page, size, city));
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody @Valid Request request) {
        return ResponseEntity.ok(svc.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable Long id, @RequestBody @Valid Request request) {
        return ResponseEntity.ok(svc.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(svc.delete(id));
    }
}