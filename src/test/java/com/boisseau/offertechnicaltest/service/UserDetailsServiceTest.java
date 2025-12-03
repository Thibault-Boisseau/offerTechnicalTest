package com.boisseau.offertechnicaltest.service;

import com.boisseau.offertechnicaltest.exception.UserNotFoundException;
import com.boisseau.offertechnicaltest.model.Gender;
import com.boisseau.offertechnicaltest.model.User;
import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserGetDetailsService userDetailsService;

    // User found
    @Test
    void getUserById_existing_user_returns_user_response() {
        User user = new User();
        user.setId(1);
        user.setName("Jean Valjean");
        user.setBirthdate(LocalDate.of(2000, 1, 1));
        user.setCountryOfResidence("FRANCE");
        user.setPhoneNumber("06 12 34 56 78");
        user.setGender(Gender.MALE);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        UserResponse response = userDetailsService.getUserById(1);

        assertEquals(1, response.id());
        assertEquals("Jean Valjean", response.name());
        assertEquals(LocalDate.of(2000, 1, 1), response.birthdate());
        assertEquals("FRANCE", response.countryOfResidence());
        assertEquals("06 12 34 56 78", response.phoneNumber());
        assertEquals(Gender.MALE, response.gender());
    }

    // User not found
    @Test
    void getUserById_non_existing_user_throws_UserNotFoundException() {
        when(userRepo.findById(999999999)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userDetailsService.getUserById(999999999));

        assertEquals("User not found with id: 999999999.", ex.getMessage());
    }
}
