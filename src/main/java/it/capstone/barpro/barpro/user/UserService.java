package it.capstone.barpro.barpro.user;

import it.capstone.barpro.barpro.email.EmailService;
import it.capstone.barpro.barpro.errors.InvalidLoginException;
import it.capstone.barpro.barpro.roles.Roles;
import it.capstone.barpro.barpro.roles.RolesRepository;
import it.capstone.barpro.barpro.security.JwtUtils;
import it.capstone.barpro.barpro.user.authDtos.LoginResponseDTO;
import it.capstone.barpro.barpro.user.authDtos.RegisterUserDTO;
import it.capstone.barpro.barpro.user.authDtos.RegisterUserModel;
import it.capstone.barpro.barpro.user.authDtos.RegisteredUserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final EmailService emailService; // per gestire invio email di benvenuto
//    private final Cloudinary cloudinary; // gestisce cloudinary

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

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            //SI EFFETTUA IL LOGIN
            //SI CREA UNA AUTENTICAZIONE OVVERO L'OGGETTO DI TIPO AUTHENTICATION
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            a.getAuthorities(); //SERVE A RECUPERARE I RUOLI/IL RUOLO

            //SI CREA UN CONTESTO DI SICUREZZA CHE SARA UTILIZZATO IN PIU OCCASIONI
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = usersRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(RegisteredUserDTO.builder()
                            .withId(user.getId())
                            .withFirstName(user.getFirstName())
                            .withLastName(user.getLastName())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();

            //UTILIZZO DI JWTUTILS PER GENERARE IL TOKEN UTILIZZANDO UNA AUTHENTICATION E LO ASSEGNA ALLA LOGINRESPONSEDTO
            dto.setToken(jwt.generateToken(a));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            //ECCEZIONE LANCIATA SE LO USERNAME E SBAGLIATO E QUINDI L'UTENTE NON VIENE TROVATO
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            //ECCEZIONE LANCIATA SE LA PASSWORD E SBAGLIATA
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
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
    public RegisteredUserDTO create(@Valid RegisterUserModel RegisterUserModel){
        User user = new User();
        BeanUtils.copyProperties(RegisterUserModel, user);
//        user.setRole(roleusersRepository.findById(1L).get());
        usersRepository.save(user);

        RegisteredUserDTO RegisteredUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, RegisteredUserDTO);

        return RegisteredUserDTO;
    }

    @Transactional
    public RegisteredUserDTO create(@Valid RegisterUserModel RegisterUserModel, boolean isAdmin){
        User user = new User();
        BeanUtils.copyProperties(RegisterUserModel, user);
//        if (isAdmin) user.setRole(roleusersRepository.findById(0L).get());
        usersRepository.save(user);

        RegisteredUserDTO RegisteredUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, RegisteredUserDTO);

        return RegisteredUserDTO;
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
        BeanUtils.copyProperties(RegisterUserModel, user);
        usersRepository.save(user);

        RegisteredUserDTO RegisteredUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(user, RegisteredUserDTO);

        return RegisteredUserDTO;
    }
}
