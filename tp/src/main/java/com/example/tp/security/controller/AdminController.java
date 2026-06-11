package com.example.tp.security.controller;

import com.example.tp.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")   // Toute la classe nécessite ADMIN
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // ── Dashboard admin : liste des utilisateurs ──────────────────────
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // ── Supprimer un utilisateur ──────────────────────────────────────
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes redirectAttrs) {
        userService.deleteUser(id);
        redirectAttrs.addFlashAttribute("successMessage", "Utilisateur supprimé.");
        return "redirect:/admin/users";
    }
}
