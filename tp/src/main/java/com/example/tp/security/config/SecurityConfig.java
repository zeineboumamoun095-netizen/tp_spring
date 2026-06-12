package com.example.tp.security.config;

import com.example.tp.security.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // active @PreAuthorize sur les controllers
public class SecurityConfig {

    // ── BCrypt pour hasher les mots de passe ──────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── Provider qui connecte UserService + PasswordEncoder ──────────
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ── Règles de sécurité ────────────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth

                        // ── Pages publiques (sans login) ──────────────────────
                        .requestMatchers(
                                "/",
                                "/auth/login",
                                "/auth/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/h2-console/**"
                        ).permitAll()

                        // ── Swagger accessible sans login ─────────────────────
                        

                        // ── API REST : lecture publique, écriture ADMIN ────────
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/products/**",
                                "/api/articles/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/products/**",
                                "/api/articles/**"
                        ).hasRole("ADMIN")

                        // ── Interface web : lecture pour USER et ADMIN ─────────
                        .requestMatchers(
                                "/products",
                                "/articles",
                                "/articles/**"           // lecture des articles
                        ).authenticated()

                        // ── Actions d'écriture web : ADMIN seulement ──────────
                        .requestMatchers(
                                "/products/new",
                                "/products/save",
                                "/products/edit/**",
                                "/products/update/**",
                                "/products/delete/**",
                                "/articles/new",
                                "/articles/save",
                                "/articles/edit/**",
                                "/articles/update/**",
                                "/articles/delete/**"
                        ).hasRole("ADMIN")

                        // ── Gestion des utilisateurs : ADMIN seulement ─────────
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ── Tout le reste nécessite une authentification ────────
                        .anyRequest().authenticated()
                )

                // ── Formulaire de login personnalisé ──────────────────────
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")       // Spring traite le POST ici
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )

                // ── Logout ────────────────────────────────────────────────
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // ── H2 console (dev uniquement) ───────────────────────────
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/api/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())  // H2 console
                );

        return http.build();
    }
}
