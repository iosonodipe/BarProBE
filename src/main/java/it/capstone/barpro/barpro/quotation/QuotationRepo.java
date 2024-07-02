package it.capstone.barpro.barpro.quotation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepo extends JpaRepository<Quotation, Long>{

    public Page<QuotationResponseProj> findAllBy(Pageable pageable);
    public Page<QuotationResponseProj> findAllByCity(Pageable pageable, String city);
    public Page<QuotationResponseProj> findAllByUserId(Pageable pageable, Long id);
    boolean existsByUserId(Long id);

}
