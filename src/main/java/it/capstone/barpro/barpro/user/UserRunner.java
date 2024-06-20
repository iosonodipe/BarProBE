package it.capstone.barpro.barpro.user;

import com.github.javafaker.Faker;
import it.capstone.barpro.barpro.role.Role;
import it.capstone.barpro.barpro.role.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UserRunner implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    private final Faker faker = new Faker();
    private final Random random = new Random();


    private final List<String> cities = List.of("Milano", "Roma", "Napoli", "Torino", "Bologna", "Bergamo", "Venezia", "Padova", "Firenze", "Pisa");

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if the admin user exists
        if (userRepo.findByRoleId(0L) == null) {
            createAdminUser();
        }

        // Check if there are already users in the database
        if (userRepo.count() < 21) {
            createRandomUsers(20);
        }
    }

    private void createAdminUser() {
        Role adminRole = roleRepo.findById(0L).orElseThrow(() -> new RuntimeException("Admin role not found"));

        User admin = new User();
        admin.setName("Admin");
        admin.setSurname("User");
        admin.setEmail("admin@admin.com");
        admin.setPassword("adminuser");
        admin.setPhoneNumber(generateItalianPhoneNumber());
        admin.setBirthDate(LocalDate.of(1980, 1, 1));
        admin.setCity("AdminCity");
        admin.setPostalCode("00000");
        admin.setRole(adminRole);

        userRepo.save(admin);
    }

    private void createRandomUsers(int count) {
        Role userRole = roleRepo.findById(1L).orElseThrow(() -> new RuntimeException("User role not found"));

        List<User> users = IntStream.range(0, count)
                .mapToObj(i -> createRandomUser(userRole))
                .collect(Collectors.toList());

        userRepo.saveAll(users);
    }

    private User createRandomUser(Role role) {
        User user = new User();
        user.setName(faker.name().firstName());
        user.setSurname(faker.name().lastName());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password(10, 15));
        user.setPhoneNumber(generateItalianPhoneNumber());
        user.setBirthDate(convertToLocalDate(faker.date().birthday()));
        user.setCity(generateRandomCity());
        user.setPostalCode(generatePostalCode());
        user.setRole(role);

        return user;
    }

    private String generateItalianPhoneNumber() {
        // Generates an Italian phone number (10 digits starting with '3')
        return "3" + faker.number().digits(9);
    }

    private String generateRandomCity() {
        // Randomly selects a city from the predefined list
        return cities.get(random.nextInt(cities.size()));
    }

    private String generatePostalCode() {
        // Generates a 5-digit postal code
        return faker.number().digits(5);
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}