package com.example.tp.security.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

// ── DTO utilisé pour le formulaire d'inscription ──────────────────
@Data
public class RegisterDto {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Au moins 6 caractères")
    private String password;

    @NotBlank(message = "La confirmation est obligatoire")
    private String confirmPassword;
}
