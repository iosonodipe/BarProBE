package it.capstone.barpro.barpro.barman;

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
public class BarmanRunner{

    @Autowired
    private BarmanRepo barmanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final List<String> ITALIAN_CITIES = Arrays.asList(
            "Roma", "Milano", "Napoli", "Torino", "Palermo",
            "Genova", "Bologna", "Firenze", "Venezia", "Verona"
    );

    @Bean
    public CommandLineRunner initBarmanDatabase() {
        return args -> {
            if (barmanRepository.count() < 20) {
                Faker faker = new Faker();

                List<Barman> barmen = IntStream.range(0, 20).mapToObj(i -> {
                    Barman barman = new Barman();
                    barman.setFirstName(faker.name().firstName());
                    barman.setLastName(faker.name().lastName());
                    barman.setUsername("barman" + i);
                    barman.setEmail("barman" + i + "@example.com");
                    barman.setPassword(passwordEncoder.encode("password"));
                    barman.setCity(ITALIAN_CITIES.get(new Random().nextInt(ITALIAN_CITIES.size())));
                    barman.setAvatar("https://picsum.photos/300");
                    barman.setExperienceYears(faker.number().numberBetween(1, 30));
                    barman.setDescription(faker.lorem().sentence(10, 20));
                    barman.setRating(faker.number().numberBetween(1, 5));

                    Roles role = new Roles();
                    role.setRoleType(Roles.ROLES_BARMAN);

                    barman.getRoles().add(role);

                    return barman;
                }).collect(Collectors.toList());

                barmanRepository.saveAll(barmen);
            }
        };
    }

}