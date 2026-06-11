package com.example.tp.product.service;

import com.example.tp.product.entity.Product;
import com.example.tp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // ── Récupérer tous les produits ──────────────────────────────────────────
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ── Récupérer un produit par ID ──────────────────────────────────────────
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // ── Créer un produit ─────────────────────────────────────────────────────
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // ── Mettre à jour un produit ─────────────────────────────────────────────
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id : " + id));
        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());
        existing.setDescription(updated.getDescription());
        return productRepository.save(existing);
    }

    // ── Supprimer un produit ─────────────────────────────────────────────────
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable avec l'id : " + id);
        }
        productRepository.deleteById(id);
    }

    // ── Recherche par nom ─────────────────────────────────────────────────────
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
