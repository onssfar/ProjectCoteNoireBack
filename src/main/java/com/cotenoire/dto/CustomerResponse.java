package com.cotenoire.dto;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String city,
        String postalCode
) {
}