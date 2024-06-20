package it.capstone.barpro.barpro.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public Page<UserResponseProj> findAllBy(Pageable pageable);
    public User findByRoleId(Long id);
}
