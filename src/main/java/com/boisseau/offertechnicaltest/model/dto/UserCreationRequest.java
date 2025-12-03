package com.boisseau.offertechnicaltest.model.dto;

import com.boisseau.offertechnicaltest.model.Gender;

import java.time.LocalDate;

public record UserCreationRequest(
        String name,
        LocalDate birthdate,
        String countryOfResidence,
        String phoneNumber,
        Gender gender
) {}
