package com.boisseau.offertechnicaltest.model.dto;

import com.boisseau.offertechnicaltest.model.Gender;

import java.time.LocalDate;

public record UserResponse(
        int id,
        String name,
        LocalDate birthdate,
        String countryOfResidence,
        String phoneNumber,
        Gender gender) {
}
