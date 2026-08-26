package com.cotenoire.controller;

import com.cotenoire.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> nf(ProductNotFoundException e) {
        return err(404, e.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> oe(OrderException e) {
        return err(400, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> ve(MethodArgumentNotValidException e) {
        Map<String, Object> r = err(400, "Données invalides.");
        Map<String, String> f = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> f.put(x.getField(), x.getDefaultMessage()));
        r.put("fields", f);
        return r;
    }

    private Map<String, Object> err(int s, String m) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("status", s);
        r.put("message", m);
        return r;
    }
}
