package it.capstone.barpro.barpro.user;

import it.capstone.barpro.barpro.role.RoleRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private RoleRepo roleRepo;

    public Page<Response> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllBy(pageable);
    }

    public Response findById(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Utente non trovata.");
        }
        User user = repo.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(user, response);
        return response;
    }
    @Transactional
    public Response create(@Valid Request request){
        User user = new User();
        BeanUtils.copyProperties(request, user);
        repo.save(user);

        Response response = new Response();
        BeanUtils.copyProperties(user, response);

        return response;
    }

    @Transactional
    public Response create(@Valid Request request, boolean isAdmin){
        User user = new User();
        BeanUtils.copyProperties(request, user);
        if (isAdmin) user.setRole(roleRepo.findById(0L).get());
        repo.save(user);

        Response response = new Response();
        BeanUtils.copyProperties(user, response);

        return response;
    }

    @Transactional
    public String delete(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Utente non trovato.");
        }
        repo.deleteById(id);
        return "Utente eliminato.";
    }

    @Transactional
    public Response update(Long id, @Valid Request request){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Utente non trovato.");
        }
        User user = repo.findById(id).get();
        BeanUtils.copyProperties(request, user);
        repo.save(user);

        Response response = new Response();
        BeanUtils.copyProperties(user, response);

        return response;
    }
}
