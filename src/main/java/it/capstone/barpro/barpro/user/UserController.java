package it.capstone.barpro.barpro.user;

import com.cloudinary.Cloudinary;
import it.capstone.barpro.barpro.errors.ApiValidationException;
import it.capstone.barpro.barpro.user.authDtos.*;
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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseWrapper> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(userService.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RegisteredUserDTO> register(@RequestBody @Validated RegisterUserModel model, BindingResult validator){
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        var registeredUser = userService.register(
                RegisterUserDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withPassword(model.password())
                        .withAvatar(model.avatar())
                        .withCity(model.city())
                        .build());

        return  new ResponseEntity<> (registeredUser, HttpStatus.OK);
    }


    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisteredUserDTO> registerAdmin(@RequestBody RegisterUserDTO registerUser){
        return ResponseEntity.ok(userService.registerAdmin(registerUser));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseProj>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.findAll(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> update(@PathVariable Long id, @RequestBody @Valid RegisterUserModel request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @PostMapping("/{username}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String username, @RequestPart("file") MultipartFile file) {
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
