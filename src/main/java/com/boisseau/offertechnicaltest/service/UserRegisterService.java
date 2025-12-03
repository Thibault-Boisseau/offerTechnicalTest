package com.boisseau.offertechnicaltest.service;

import com.boisseau.offertechnicaltest.model.User;
import com.boisseau.offertechnicaltest.model.dto.UserCreationRequest;
import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserRegisterService {

    @Autowired
    private UserRepo userRepo;

    public UserResponse addUser(UserCreationRequest request) throws IllegalArgumentException {

        if (!(request.phoneNumber() == null) && userRepo.existsByPhoneNumber(request.phoneNumber())){
            throw new IllegalArgumentException("This number is already used.");
        }

        verifyUserName(request.name());
        verifyUserBirthdate(request.birthdate());
        verifyUserCountryOfResidence(request.countryOfResidence());
        verifyUserPhoneNumber(request.phoneNumber());

        User user = new User();
        user.setName(request.name());
        user.setBirthdate(request.birthdate());
        user.setCountryOfResidence(request.countryOfResidence());
        user.setPhoneNumber(request.phoneNumber());
        user.setGender(request.gender());

        User saved = userRepo.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getBirthdate(),
                saved.getCountryOfResidence(),
                saved.getPhoneNumber(),
                saved.getGender()
        );
    }


    public void verifyUserName(String name) throws IllegalArgumentException {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name is required.");
        }
    }

    public void verifyUserCountryOfResidence(String countryOfResidence) throws IllegalArgumentException {
        if (countryOfResidence == null || countryOfResidence.isBlank()){
            throw new IllegalArgumentException("Country of residence is required.");
        }
        if (!"FRANCE".equalsIgnoreCase(countryOfResidence)){
            throw new IllegalArgumentException("Only users from France are allowed.");
        }
    }

    public void verifyUserPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (!(phoneNumber == null) && !phoneNumber.replace(" ","").matches("^((\\+)33|0)[1-9](\\d{2}){4}$")){
            throw new IllegalArgumentException("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX.");
        }
    }

    public void verifyUserBirthdate(LocalDate birthdate) throws IllegalArgumentException {
        if (birthdate == null) {
            throw new IllegalArgumentException("Birthdate is required.");
        }
        if (birthdate.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthdate cannot be in the future.");
        }
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        if (age <18) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }
    }
}
