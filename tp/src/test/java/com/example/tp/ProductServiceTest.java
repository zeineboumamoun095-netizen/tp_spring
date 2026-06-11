package com.example.tp;

import com.example.tp.product.entity.Product;
import com.example.tp.product.repository.ProductRepository;
import com.example.tp.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - ProductService")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop Test")
                .price(999.99)
                .description("Un ordinateur de test")
                .build();
    }

    @Test
    @DisplayName("getAllProducts() doit retourner la liste complète")
    void getAllProducts_ShouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Laptop Test");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getProductById() doit retourner le produit si trouvé")
    void getProductById_WhenExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("getProductById() doit retourner empty si non trouvé")
    void getProductById_WhenNotExists_ShouldReturnEmpty() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("createProduct() doit sauvegarder et retourner le produit")
    void createProduct_ShouldSaveAndReturn() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product result = productService.createProduct(sampleProduct);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Laptop Test");
        verify(productRepository).save(sampleProduct);
    }

    @Test
    @DisplayName("deleteProduct() doit lever une exception si le produit n'existe pas")
    void deleteProduct_WhenNotExists_ShouldThrowException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("updateProduct() doit mettre à jour les champs")
    void updateProduct_WhenExists_ShouldUpdateFields() {
        Product updatedData = Product.builder()
                .name("Laptop Updated")
                .price(1199.99)
                .description("Description mise à jour")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product result = productService.updateProduct(1L, updatedData);

        verify(productRepository).save(any(Product.class));
        assertThat(sampleProduct.getName()).isEqualTo("Laptop Updated");
        assertThat(sampleProduct.getPrice()).isEqualTo(1199.99);
    }
}
