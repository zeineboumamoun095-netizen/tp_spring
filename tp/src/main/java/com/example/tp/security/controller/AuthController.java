package com.example.tp.security.controller;

import com.example.tp.security.dto.RegisterDto;
import com.example.tp.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ── GET /auth/login ───────────────────────────────────────────────
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Vous avez été déconnecté avec succès.");
        }
        return "auth/login";
    }
    

        @GetMapping("/auth/login")
        public String loginPage() {
            return "login";
        }



    // ── GET /auth/register ────────────────────────────────────────────
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }


    // ── POST /auth/register ───────────────────────────────────────────
    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(dto);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Compte créé avec succès ! Vous pouvez maintenant vous connecter.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
