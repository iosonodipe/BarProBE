package it.capstone.barpro.barpro.quotation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepo extends JpaRepository<Quotation, Long> {

    public List<QuotationResponseProj> findAllBy();
    public List<QuotationResponseProj> findAllByCity(String city);

}
