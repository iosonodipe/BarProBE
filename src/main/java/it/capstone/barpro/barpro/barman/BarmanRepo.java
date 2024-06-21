package it.capstone.barpro.barpro.barman;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarmanRepo extends JpaRepository<Barman, Long> {

    public Page<BarmanResponseProj> findAllBy(Pageable pageable);
    public Page<BarmanResponseProj> findAllByCity(Pageable pageable, String city);
    Optional<Barman> findOneByUsername(String username);
    Optional<Barman> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

