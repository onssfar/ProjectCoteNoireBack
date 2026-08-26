package com.cotenoire.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String m) {
        super(m);
    }
}
