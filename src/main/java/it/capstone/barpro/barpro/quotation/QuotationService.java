package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.UserRepo;
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

import java.util.List;

@Service
@Validated
public class QuotationService {

    @Autowired
    QuotationRepo repo;

    @Autowired
    UserRepo userRepo;

    public Page<QuotationResponseProj> findAll(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAllBy(pageable);
    }

    public Page<QuotationResponseProj> findAllByCity(int page, int size, String city){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllByCity(pageable, city);
    }

    public Response findById(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        Quotation quotation = repo.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);
        return response;
    }
    @Transactional
    public Response create(@Valid Request request){
        Quotation quotation = new Quotation();
        BeanUtils.copyProperties(request, quotation);
        quotation.setStatus(Status.OPEN);
        quotation.setUser(userRepo.findById(request.getIdUser()).get());
        repo.save(quotation);

        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);

        it.capstone.barpro.barpro.user.Response user = new it.capstone.barpro.barpro.user.Response();
        BeanUtils.copyProperties(quotation.getUser(), user);
        response.setUser(user);

        return response;
    }

    @Transactional
    public String delete(Long id){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        repo.deleteById(id);
        return "Richiesta di quotazione eliminata.";
    }

    @Transactional
    public Response update(Long id, @Valid Request request){
        if (!repo.existsById(id)){
            throw new EntityNotFoundException("Richiesta di quotazione non trovata.");
        }
        Quotation quotation = repo.findById(id).get();
        BeanUtils.copyProperties(request, quotation);
        repo.save(quotation);

        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);

        it.capstone.barpro.barpro.user.Response user = new it.capstone.barpro.barpro.user.Response();
        BeanUtils.copyProperties(quotation.getUser(), user);
        response.setUser(user);

        return response;
    }
}
