package com.boisseau.offertechnicaltest.service;

import com.boisseau.offertechnicaltest.exception.UserNotFoundException;
import com.boisseau.offertechnicaltest.model.User;
import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGetDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserResponse getUserById(int id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id +"."));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getBirthdate(),
                user.getCountryOfResidence(),
                user.getPhoneNumber(),
                user.getGender()
        );
    }
}
