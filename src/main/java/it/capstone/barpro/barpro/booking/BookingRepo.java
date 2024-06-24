package it.capstone.barpro.barpro.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    public Page<BookingResponseProj> findAllBy(Pageable pageable);
    public Page<BookingResponseProj> findAllByUserId(Pageable pageable, Long id);
    public List<BookingResponseProj> findAllByUserId(Long id);
    public Page<BookingResponseProj> findAllByBarmanId(Pageable pageable, Long id);
}
