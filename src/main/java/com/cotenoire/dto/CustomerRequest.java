package com.cotenoire.dto;

import jakarta.validation.constraints.*;

public record CustomerRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email,
                              @NotBlank String phone, @NotBlank String address, @NotBlank String city,
                              String postalCode) {
}
