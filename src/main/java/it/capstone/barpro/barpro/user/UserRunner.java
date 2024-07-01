package it.capstone.barpro.barpro.user;

import com.github.javafaker.Faker;
import it.capstone.barpro.barpro.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class UserRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ITALIAN_CITIES = Arrays.asList(
            "Roma", "Milano", "Napoli", "Torino", "Palermo",
            "Genova", "Bologna", "Firenze", "Venezia", "Verona"
    );

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            if (userRepository.count() < 50) {
                Faker faker = new Faker();

                List<User> users = IntStream.range(0, 50).mapToObj(i -> {
                    User user = new User();
                    user.setFirstName(faker.name().firstName());
                    user.setLastName(faker.name().lastName());
                    user.setUsername("user" + i);
                    user.setEmail("user" + i + "@example.com");
                    user.setPassword(passwordEncoder.encode("password"));
                    user.setCity(ITALIAN_CITIES.get(new Random().nextInt(ITALIAN_CITIES.size())));
                    user.setAvatar("https://picsum.photos/300");

                    Roles role = new Roles();
                    role.setRoleType(Roles.ROLES_USER);

                    user.getRoles().add(role);

                    if (i == 0) {
                        user.setUsername("admin");
                        role.setRoleType(Roles.ROLES_ADMIN);
                    }

                    return user;
                }).collect(Collectors.toList());

                userRepository.saveAll(users);
            }
        };
    }
}