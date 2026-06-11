package com.example.tp.security.config;

import com.example.tp.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserService userService;

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            // Créer l'admin par défaut s'il n'existe pas
            var admin = userService.createAdmin(
                    "admin",
                    "admin@springtp.com",
                    "admin123"
            );
            if (admin != null) {
                log.info("=== Compte ADMIN créé : username=admin / password=admin123 ===");
            }

            // Créer un USER de démonstration
            var demoUser = userService.createAdmin(
                    "user",
                    "user@springtp.com",
                    "user123"
            );
            // On change le rôle manuellement pour USER
            // (createAdmin reused for simplicity — see UserService)
            if (demoUser != null) {
                demoUser.setRole(com.example.tp.security.entity.Role.USER);
                log.info("=== Compte USER créé : username=user / password=user123 ===");
            }
        };
    }
}
