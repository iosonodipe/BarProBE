package it.capstone.barpro.barpro.user;

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

    public Page<Response> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        return repo.findAllBy(pageable);
    }
}
