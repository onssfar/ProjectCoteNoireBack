package com.cotenoire.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    private String scent;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal price;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long v) {
        id = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        name = v;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String v) {
        category = v;
    }

    public String getScent() {
        return scent;
    }

    public void setScent(String v) {
        scent = v;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal v) {
        price = v;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer v) {
        stock = v;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean v) {
        active = v;
    }
}