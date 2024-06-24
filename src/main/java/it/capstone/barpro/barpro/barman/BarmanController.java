package it.capstone.barpro.barpro.barman;

import com.cloudinary.Cloudinary;
import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanDTO;
import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanModel;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.errors.ApiValidationException;
import it.capstone.barpro.barpro.user.User;
import it.capstone.barpro.barpro.user.UserRepository;
import it.capstone.barpro.barpro.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/barmen")
public class BarmanController {

    @Autowired
    private BarmanService svc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private Cloudinary cloudinary;

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

    @PostMapping("/{username}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            // Carica l'immagine su Cloudinary
            var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    com.cloudinary.utils.ObjectUtils.asMap("public_id", username + "_avatar"));

            // Recupera l'URL dell'immagine
            String url = uploadResult.get("url").toString();

            // Aggiorna l'utente con l'URL dell'immagine avatar
            Optional<User> userOptional = usersRepository.findOneByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setAvatar(url);
                usersRepository.save(user);
                return ResponseEntity.ok(url);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
        }
    }

    @PutMapping("/{username}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        Optional<User> userOptional = usersRepository.findOneByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                // Aggiorna l'avatar dell'utente
                String newAvatarUrl = userService.updateAvatar(user.getId(), file);
                return ResponseEntity.ok(newAvatarUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update avatar");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{username}/avatar")
    public ResponseEntity<String> getUserAvatar(@PathVariable String username) {
        Optional<User> user = usersRepository.findOneByUsername(username);
        if (user.isPresent() && user.get().getAvatar() != null) {
            return ResponseEntity.ok(user.get().getAvatar());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found");
        }
    }
}