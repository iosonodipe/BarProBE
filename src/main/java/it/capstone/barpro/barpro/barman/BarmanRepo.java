package it.capstone.barpro.barpro.barman;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarmanRepo extends JpaRepository<Barman, Long> {
}
