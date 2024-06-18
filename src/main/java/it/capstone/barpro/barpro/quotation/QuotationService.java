package it.capstone.barpro.barpro.quotation;

import it.capstone.barpro.barpro.user.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<QuotationResponseProj> findAll(){
        return repo.findAllBy();
    }

    public List<QuotationResponseProj> findAllByCity(String city){
        return repo.findAllByCity(city);
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
        quotation.setUser(userRepo.findById(request.getIdUser()).get());
        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);
        repo.save(quotation);
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
        Response response = new Response();
        BeanUtils.copyProperties(quotation, response);
        repo.save(quotation);
        return response;
    }
}
