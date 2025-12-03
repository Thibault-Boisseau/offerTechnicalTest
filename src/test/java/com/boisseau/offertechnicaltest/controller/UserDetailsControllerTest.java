package com.boisseau.offertechnicaltest.controller;

import com.boisseau.offertechnicaltest.OfferTechnicalTestApplication;
import com.boisseau.offertechnicaltest.model.Gender;
import com.boisseau.offertechnicaltest.model.User;
import com.boisseau.offertechnicaltest.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { OfferTechnicalTestApplication.class })
@WebAppConfiguration
class UserDetailsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepo userRepo;

    private MockMvc mockMvc;

    // reset repository
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        userRepo.deleteAll();
    }

    // User found : 200 OK
    @Test
    void getUserById_existing_user_returns_200() throws Exception {

        User user = new User();
        user.setName("Jean Valjean");
        user.setBirthdate(LocalDate.of(2000, 1, 1));
        user.setCountryOfResidence("France");
        user.setPhoneNumber("06 12 34 56 78");
        user.setGender(Gender.MALE);

        User saved = userRepo.save(user);

        mockMvc.perform(get("/api/user/{id}", saved.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Jean Valjean"))
                .andExpect(jsonPath("$.birthdate").value("2000-01-01"))
                .andExpect(jsonPath("$.countryOfResidence").value("France"))
                .andExpect(jsonPath("$.phoneNumber").value("06 12 34 56 78"))
                .andExpect(jsonPath("$.gender").value("Male"));
    }

    // User not found : 404
    @Test
    void getUserById_non_existing_user_returns_404_with_message() throws Exception {
        userRepo.deleteAll();

        mockMvc.perform(get("/api/user/{id}", 999999999)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message").value("User not found with id: 999999999."));
    }
}