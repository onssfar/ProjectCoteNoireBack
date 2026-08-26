package com.cotenoire.service;

import com.cotenoire.entity.Product;
import com.cotenoire.exception.ProductNotFoundException;
import com.cotenoire.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findByActiveTrue();
    }

    public Product find(Long id) {
        return productRepository.findById(id).filter(Product::getActive).orElseThrow(() -> new ProductNotFoundException("Produit introuvable : " + id));
    }
    @Transactional
    public Product createProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire.");
        }

        if (product.getPrice() == null || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur ou égal à 0.");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Le stock doit être supérieur ou égal à 0.");
        }

        if (product.getActive() == null) {
            product.setActive(true);
        }

        return productRepository.save(product);
    }
}
