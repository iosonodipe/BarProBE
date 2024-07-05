package it.capstone.barpro.barpro.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.capstone.barpro.barpro.barman.BarmanRepo;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.email.EmailService;
import it.capstone.barpro.barpro.errors.FileSizeExceededException;
import it.capstone.barpro.barpro.errors.InvalidLoginException;
import it.capstone.barpro.barpro.roles.Roles;
import it.capstone.barpro.barpro.roles.RolesRepository;
import it.capstone.barpro.barpro.security.JwtUtils;
import it.capstone.barpro.barpro.user.authDtos.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository usersRepository;
    private final BarmanRepo barmanRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final EmailService emailService; // per gestire invio email di benvenuto
    private final Cloudinary cloudinary; // gestisce cloudinary
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public Page<UserResponseProj> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return usersRepository.findAllBy(pageable);
    }

    public RegisteredUserDTO findById(Long id){
        if (!usersRepository.existsById(id)){
            throw new EntityNotFoundException("Utente non trovata.");
        }
        User user = usersRepository.findById(id).get();
        RegisteredUserDTO RegisteredUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, RegisteredUserDTO);
        return RegisteredUserDTO;
    }

    public Optional<LoginResponseWrapper> login(String username, String password) {
        try {
            //SI EFFETTUA IL LOGIN
            //SI CREA UNA AUTENTICAZIONE OVVERO L'OGGETTO DI TIPO AUTHENTICATION
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            a.getAuthorities(); //SERVE A RECUPERARE I RUOLI/IL RUOLO

            //SI CREA UN CONTESTO DI SICUREZZA CHE SARA UTILIZZATO IN PIU OCCASIONI
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = usersRepository.findOneByUsername(username).orElseThrow();
            if (user.getRoles().contains(new Roles(Roles.ROLES_USER)) || user.getRoles().contains(new Roles(Roles.ROLES_ADMIN))){
                var dto = LoginResponseDTO.builder()
                        .withUser(RegisteredUserDTO.builder()
                                .withId(user.getId())
                                .withFirstName(user.getFirstName())
                                .withLastName(user.getLastName())
                                .withEmail(user.getEmail())
                                .withRoles(user.getRoles())
                                .withUsername(user.getUsername())
                                .withCity(user.getCity())
                                .build())
                        .build();
                dto.setToken(jwt.generateToken(a));
                return Optional.of(new LoginResponseWrapper(dto));
            } else if (user.getRoles().contains(new Roles(Roles.ROLES_BARMAN))){
                var barman = barmanRepository.findById(user.getId()).get();
                var dto = it.capstone.barpro.barpro.barman.authDtos.LoginResponseDTO.builder()
                        .withBarman(RegisteredBarmanDTO.builder()
                                .withId(user.getId())
                                .withFirstName(user.getFirstName())
                                .withLastName(user.getLastName())
                                .withEmail(user.getEmail())
                                .withCity(user.getCity())
                                .withRoles(user.getRoles())
                                .withUsername(user.getUsername())
                                .withRating(barman.getRating())
                                .withExperienceYears(barman.getExperienceYears())
                                .withDescription(barman.getDescription())
                                .build())
                        .build();
                dto.setToken(jwt.generateToken(a));
                return Optional.of(new LoginResponseWrapper(dto));
            }

            //UTILIZZO DI JWTUTILS PER GENERARE IL TOKEN UTILIZZANDO UNA AUTHENTICATION E LO ASSEGNA ALLA LOGINRESPONSEDTO

        } catch (NoSuchElementException e) {
            //ECCEZIONE LANCIATA SE LO USERNAME E SBAGLIATO E QUINDI L'UTENTE NON VIENE TROVATO
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            //ECCEZIONE LANCIATA SE LA PASSWORD E SBAGLIATA
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
        return Optional.empty();
    }

    public RegisteredUserDTO register(RegisterUserDTO register){
        if(usersRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(usersRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_USER).get();

        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        usersRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());
        return response;
    }

    public RegisteredUserDTO registerAdmin(RegisterUserDTO register){
        if(usersRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(usersRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_ADMIN).get();
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        usersRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        return response;
    }

    @Transactional
    public String delete(Long id){
        if (!usersRepository.existsById(id)){
            throw new EntityNotFoundException("Utente non trovato.");
        }
        usersRepository.deleteById(id);
        return "Utente eliminato.";
    }

    @Transactional
    public RegisteredUserDTO update(Long id, @Valid RegisterUserModel RegisterUserModel){
        if (!usersRepository.existsById(id)){
            throw new EntityNotFoundException("Utente non trovato.");
        }
        User user = usersRepository.findById(id).get();
        String password = user.getPassword();
        String avatar = user.getAvatar();
        BeanUtils.copyProperties(RegisterUserModel, user);
        user.setAvatar(avatar);
        if (RegisterUserModel.password().equals("0")) {
            user.setPassword(password);
        } else user.setPassword(encoder.encode(RegisterUserModel.password()));
        usersRepository.save(user);

        RegisteredUserDTO RegisteredUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, RegisteredUserDTO);

        return RegisteredUserDTO;
    }

    @Transactional
    public String uploadAvatar(Long id, MultipartFile image) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (image.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        Optional<User> optionalUser = usersRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String existingPublicId = user.getAvatar();
        if (existingPublicId != null && !existingPublicId.isEmpty()) {
            cloudinary.uploader().destroy(existingPublicId, ObjectUtils.emptyMap());
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");

        user.setAvatar(publicId);
        usersRepository.save(user);

        return url;
    }

    // PUT update cloudinary file
    @Transactional
    public String updateAvatar(Long id, MultipartFile updatedImage) throws IOException {
        // Recupera l'utente dal database
        User user = usersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Elimina l'avatar esistente (se presente)
        deleteAvatar(user.getAvatar());

        // Carica la nuova immagine su Cloudinary
        var uploadResult = cloudinary.uploader().upload(updatedImage.getBytes(),
                com.cloudinary.utils.ObjectUtils.asMap("public_id", user.getUsername() + "_avatar"));

        // Recupera l'URL dell'immagine
        String url = uploadResult.get("url").toString();

        // Aggiorna l'utente con l'URL dell'avatar
        user.setAvatar(url);
        usersRepository.save(user);

        return url;
    }

    public void deleteAvatar(String url) throws IOException {
        int lastSlashIndex = url.lastIndexOf('/');
        int lastDotIndex = url.lastIndexOf('.');

        if (lastSlashIndex != -1 && lastDotIndex != -1 && lastSlashIndex < lastDotIndex) {
            String publicId = url.substring(lastSlashIndex + 1, lastDotIndex);
            cloudinary.uploader().destroy(publicId, com.cloudinary.utils.ObjectUtils.emptyMap());
        } else {
            // Gestisci il caso in cui gli indici non sono validi
            System.out.println("Indici non validi per la URL: " + url);
        }
    }


    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }
}
