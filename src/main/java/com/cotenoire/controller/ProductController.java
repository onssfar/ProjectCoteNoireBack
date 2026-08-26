package com.cotenoire.controller;

import com.cotenoire.entity.Product;
import com.cotenoire.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @GetMapping
    public List<Product> all() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product one(@PathVariable Long id) {
        return productService.find(id);
    }
}
