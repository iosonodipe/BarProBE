package it.capstone.barpro.barpro.barman;

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
public class BarmanService {

    @Autowired
    private BarmanRepo repo;

    public Page<BarmanResponseProj> findAll(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repo.findAllBy(pageable);
    }

    public Page<BarmanResponseProj> findAllByCity(int page, int size, String city){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllByCity(pageable, city);
    }

    public Response findById(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Barman non trovato.");
        }
        Barman barman = repo.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(barman, response);
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
    public Response update(Long id, @Valid Request request) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Barman non trovato.");
        }
        Barman barman = repo.findById(id).get();
        BeanUtils.copyProperties(request, barman);
        repo.save(barman);

        Response response = new Response();
        BeanUtils.copyProperties(barman, response);

        return response;
    }
}