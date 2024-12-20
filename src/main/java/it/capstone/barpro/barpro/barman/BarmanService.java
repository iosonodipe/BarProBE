package it.capstone.barpro.barpro.barman;

import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanDTO;
import it.capstone.barpro.barpro.barman.authDtos.RegisterBarmanModel;
import it.capstone.barpro.barpro.barman.authDtos.RegisteredBarmanDTO;
import it.capstone.barpro.barpro.email.EmailService;
import it.capstone.barpro.barpro.roles.Roles;
import it.capstone.barpro.barpro.roles.RolesRepository;
import it.capstone.barpro.barpro.user.UserRepository;
import it.capstone.barpro.barpro.user.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class BarmanService {

    private final PasswordEncoder encoder;
    private final BarmanRepo repo;
    private final RolesRepository rolesRepository;
    private final UserRepository usersRepository;
    private final UserService usersService;
    private final EmailService emailService; // per gestire invio email di benvenuto


    public Page<BarmanResponseProj> findAll(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAllBy(pageable);
    }

    public Page<BarmanResponseProj> findAllByCity(int page, int size, String city){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllByCity(pageable, city);
    }

    public RegisteredBarmanDTO findById(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Barman non trovato.");
        }
        Barman barman = repo.findById(id).get();
        RegisteredBarmanDTO response = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(barman, response);
        return response;
    }

    public RegisteredBarmanDTO registerBarman(RegisterBarmanDTO register){
        if(usersRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(usersRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Roles roles = rolesRepository.findById(Roles.ROLES_BARMAN).get();

        Barman barman = new Barman();
        BeanUtils.copyProperties(register, barman);
        barman.setPassword(encoder.encode(register.getPassword()));
        barman.getRoles().add(roles);
        barman.setExperienceYears(register.getExperienceYears());
        barman.setDescription(register.getDescription());
        barman.setRating(register.getRating());

        usersRepository.save(barman);

        RegisteredBarmanDTO response = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(barman, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(barman.getEmail());
        return response;
    }

    @Transactional
    public String delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Barman non trovato.");
        }
        repo.deleteById(id);
        return "Barman eliminato.";
    }

    @Transactional
    public RegisteredBarmanDTO update(Long id, @Valid RegisterBarmanModel request) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Barman non trovato.");
        }
        Barman barman = repo.findById(id).get();
        String password = barman.getPassword();
        String avatar = barman.getAvatar();
        BeanUtils.copyProperties(request, barman);
        barman.setAvatar(avatar);
        if (request.password().equals("0")) {
            barman.setPassword(password);
        } else barman.setPassword(encoder.encode(request.password()));
        repo.save(barman);

        RegisteredBarmanDTO response = new RegisteredBarmanDTO();
        BeanUtils.copyProperties(barman, response);

        return response;
    }

    @Transactional
    public String uploadAvatar(Long id, MultipartFile image) throws IOException {
        return usersService.uploadAvatar(id, image);
    }

    @Transactional
    public String updateAvatar(Long id, MultipartFile updatedImage) throws IOException {
        return usersService.updateAvatar(id, updatedImage);
    }
}