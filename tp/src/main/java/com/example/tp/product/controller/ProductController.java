package com.example.tp.product.controller;

import com.example.tp.product.entity.Product;
import com.example.tp.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API de gestion des produits")
public class ProductController {

    private final ProductService productService;

    // ── GET /api/products ────────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Lister tous les produits")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ── GET /api/products/{id} ───────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── POST /api/products ───────────────────────────────────────────────────
    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product saved = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ── PUT /api/products/{id} ───────────────────────────────────────────────
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // ── DELETE /api/products/{id} ────────────────────────────────────────────
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ── GET /api/products/search?name=xx ─────────────────────────────────────
    @GetMapping("/search")
    @Operation(summary = "Rechercher des produits par nom")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }
}
