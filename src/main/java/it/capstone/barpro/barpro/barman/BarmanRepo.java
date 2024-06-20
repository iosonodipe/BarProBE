package it.capstone.barpro.barpro.barman;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarmanRepo extends JpaRepository<Barman, Long> {

    public Page<BarmanResponseProj> findAllBy(Pageable pageable);
    public Page<BarmanResponseProj> findAllByCity(Pageable pageable, String city);
}
