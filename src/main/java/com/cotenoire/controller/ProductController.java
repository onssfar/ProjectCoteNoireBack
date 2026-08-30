package com.cotenoire.controller;

import com.cotenoire.entity.Product;
import com.cotenoire.service.ProductService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // =====================================================
    // AJOUTER UN PRODUIT
    // =====================================================

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestParam String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String scent,
            @RequestParam String price,
            @RequestParam Integer stock,
            @RequestParam Boolean active,
            @RequestParam(required = false) String description,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {

        Product product = new Product();

        product.setName(name);
        product.setCategory(category);
        product.setScent(scent);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setActive(active);
        product.setDescription(description);

        if (image != null && !image.isEmpty()) {

            product.setImageData(
                    image.getBytes()
            );

            product.setImageContentType(
                    image.getContentType()
            );
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productService.createProduct(product)
                );
    }


    // =====================================================
    // RECUPERER TOUS LES PRODUITS
    // =====================================================

    @GetMapping
    public List<Product> all() {
        return productService.findAll();
    }


    // =====================================================
    // RECUPERER UN PRODUIT
    // =====================================================

    @GetMapping("/{id}")
    public Product one(
            @PathVariable Long id
    ) {
        return productService.find(id);
    }


    // =====================================================
    // RECUPERER L'IMAGE
    // =====================================================

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> image(
            @PathVariable Long id
    ) {

        Product product =
                productService.find(id);

        if (product.getImageData() == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        if (product.getImageContentType() != null) {

            try {

                mediaType =
                        MediaType.parseMediaType(
                                product.getImageContentType()
                        );

            } catch (Exception ignored) {
            }
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(product.getImageData());
    }


    // =====================================================
    // MODIFIER UN PRODUIT
    // =====================================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Product> updateProduct(

            @PathVariable Long id,

            @RequestParam String name,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            String scent,

            @RequestParam String price,

            @RequestParam Integer stock,

            @RequestParam Boolean active,

            @RequestParam(required = false)
            String description,

            @RequestPart(required = false)
            MultipartFile image

    ) throws IOException {

        Product product =
                productService.find(id);

        // -----------------------------
        // Informations du produit
        // -----------------------------

        product.setName(name);

        product.setCategory(category);

        product.setScent(scent);

        product.setPrice(
                new BigDecimal(price)
        );

        product.setStock(stock);

        product.setActive(active);

        product.setDescription(description);


        // -----------------------------
        // Image
        // -----------------------------

        /*
         * Si une nouvelle image est envoyée,
         * on la remplace.
         *
         * Si aucune image n'est envoyée,
         * l'ancienne image est conservée.
         */

        if (image != null && !image.isEmpty()) {

            product.setImageData(
                    image.getBytes()
            );

            product.setImageContentType(
                    image.getContentType()
            );
        }


        Product updated =
                productService.updateProduct(product);

        return ResponseEntity.ok(updated);
    }


    // =====================================================
    // SUPPRIMER UN PRODUIT
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}