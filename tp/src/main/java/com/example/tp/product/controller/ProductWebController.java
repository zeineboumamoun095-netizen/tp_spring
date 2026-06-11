package com.example.tp.product.controller;

import com.example.tp.product.entity.Product;
import com.example.tp.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductWebController {

    private final ProductService productService;

    // ── Liste des produits ───────────────────────────────────────────────────
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("total", productService.getAllProducts().size());
        return "products/list";
    }

    // ── Formulaire de création ───────────────────────────────────────────────
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    // ── Sauvegarder un produit ───────────────────────────────────────────────
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
                              BindingResult result,
                              RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "products/form";
        }
        productService.createProduct(product);
        redirectAttrs.addFlashAttribute("successMessage", "Produit ajouté avec succès !");
        return "redirect:/products";
    }

    // ── Formulaire d'édition ─────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        model.addAttribute("product", product);
        return "products/form";
    }

    // ── Mettre à jour ────────────────────────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Product product,
                                BindingResult result,
                                RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "products/form";
        }
        productService.updateProduct(id, product);
        redirectAttrs.addFlashAttribute("successMessage", "Produit mis à jour !");
        return "redirect:/products";
    }

    // ── Supprimer ────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        productService.deleteProduct(id);
        redirectAttrs.addFlashAttribute("successMessage", "Produit supprimé !");
        return "redirect:/products";
    }
}
