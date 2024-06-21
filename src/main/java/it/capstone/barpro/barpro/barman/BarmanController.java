package it.capstone.barpro.barpro.barman;

import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanDTO;
import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanModel;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.errors.ApiValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barmen")
public class BarmanController {

    @Autowired
    private BarmanService svc;

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredBarmanDTO> findById(@PathVariable Long id) {
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
    public ResponseEntity<RegisteredBarmanDTO> registerBarman(@RequestBody @Validated RegisterBarmanModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }

        var registeredBarman = svc.registerBarman(
                RegisterBarmanDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withPassword(model.password())
                        .withCity(model.city())
                        .withAvatar(model.avatar())
                        .withExperienceYears(model.experienceYears())
                        .withDescription(model.description())
                        .withRating(0)
                        .build());

        return new ResponseEntity<>(registeredBarman, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisteredBarmanDTO> update(@PathVariable Long id, @RequestBody @Valid RegisterBarmanModel request) {
        return ResponseEntity.ok(svc.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(svc.delete(id));
    }
}