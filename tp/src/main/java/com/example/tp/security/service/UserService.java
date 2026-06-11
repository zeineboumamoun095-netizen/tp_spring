package com.example.tp.security.service;

import com.example.tp.security.entity.AppUser;
import com.example.tp.security.entity.Role;
import com.example.tp.security.dto.RegisterDto;
import com.example.tp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ── Appelé par Spring Security à chaque tentative de login ───────
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur introuvable : " + username));
    }

    // ── Inscription d'un nouvel utilisateur (rôle USER par défaut) ───
    public AppUser register(RegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ce nom d'utilisateur est déjà pris.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }

        AppUser user = AppUser.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)   // Les nouveaux inscrits sont USER par défaut
                .build();

        return userRepository.save(user);
    }

    // ── Créer un admin (usage interne / DataInitializer) ─────────────
    public AppUser createAdmin(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) return null;

        AppUser admin = AppUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.ADMIN)
                .build();

        return userRepository.save(admin);
    }

    // ── Liste de tous les utilisateurs (usage ADMIN) ──────────────────
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    // ── Supprimer un utilisateur ──────────────────────────────────────
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}