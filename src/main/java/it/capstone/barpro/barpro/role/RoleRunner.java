package it.capstone.barpro.barpro.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleRunner implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ADMIN") == null) {
            roleRepository.save(new Role(0L, "ADMIN"));
        }
        if (roleRepository.findByName("USER") == null) {
            roleRepository.save(new Role(1L, "USER"));
        }
        if (roleRepository.findByName("BARMAN") == null) {
            roleRepository.save(new Role(2L, "BARMAN"));
        }
    }
}
