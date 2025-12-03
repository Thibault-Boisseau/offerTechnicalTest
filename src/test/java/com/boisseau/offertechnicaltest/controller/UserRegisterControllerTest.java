package com.boisseau.offertechnicaltest.controller;

import com.boisseau.offertechnicaltest.OfferTechnicalTestApplication;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { OfferTechnicalTestApplication.class })
@WebAppConfiguration
class UserRegisterControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepo userRepo;

    private MockMvc mockMvc;

    // Reset repository
    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        userRepo.deleteAll();
    }

    // Tests about username
    @Test
    void addUser_valid_name_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Jean Valjean"))
                .andExpect(jsonPath("$.birthdate").value("2000-01-01"))
                .andExpect(jsonPath("$.countryOfResidence").value("France"))
                .andExpect(jsonPath("$.phoneNumber").value("06 12 34 56 78"))
                .andExpect(jsonPath("$.gender").value("Male"));

        List<User> allUsers = userRepo.findAll();
        assertEquals(1, allUsers.size());
        assertEquals("Jean Valjean", allUsers.get(0).getName());
    }

    @Test
    void addUser_invalid_name_null_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": null,
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Name is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_invali_name_empty_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Name is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_invalid_name_blank_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": " ",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Name is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    // Tests about country of residence
    @Test
    void addUser_country_France_ok_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryOfResidence").value("France"));

        List<User> allUsers = userRepo.findAll();
        assertEquals(1, allUsers.size());
        assertEquals("France", allUsers.get(0).getCountryOfResidence());
    }

    @Test
    void addUser_country_France_mixed_case_ok_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "FrAnCe",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryOfResidence").value("FrAnCe"));

        List<User> allUsers = userRepo.findAll();
        assertEquals(1, allUsers.size());
        assertEquals("FrAnCe", allUsers.get(0).getCountryOfResidence());
    }

    @Test
    void addUser_other_country_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "Belgium",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Only users from France are allowed."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_country_null_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": null,
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Country of residence is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_country_empty_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Country of residence is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_country_blank_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "   ",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Country of residence is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    // Tests about phone number
    @Test
    void addUser_phone_valid_national_compact_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "0612345678",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value("0612345678"));

        assertEquals("0612345678", userRepo.findAll().get(0).getPhoneNumber());
    }

    @Test
    void addUser_phone_valid_national_with_spaces_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value("06 12 34 56 78"));

        assertEquals("06 12 34 56 78", userRepo.findAll().get(0).getPhoneNumber());
    }

    @Test
    void addUser_phone_valid_international_compact_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "+33612345678",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value("+33612345678"));

        assertEquals("+33612345678", userRepo.findAll().get(0).getPhoneNumber());
    }

    @Test
    void addUser_phone_valid_international_with_spaces_returns_201() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "+33 6 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").value("+33 6 12 34 56 78"));

        assertEquals("+33 6 12 34 56 78", userRepo.findAll().get(0).getPhoneNumber());
    }

    @Test
    void addUser_phone_null_returns_201_and_is_null_in_db() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": null,
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phoneNumber").doesNotExist());

        User saved = userRepo.findAll().get(0);
        assertNull(saved.getPhoneNumber());
    }

    @Test
    void addUser_phone_empty_returns_400() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "2000-01-01",
          "countryOfResidence": "France",
          "phoneNumber": "",
          "gender": "Male"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_phone_blank_returns_400() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "2000-01-01",
          "countryOfResidence": "France",
          "phoneNumber": "   ",
          "gender": "Male"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_phone_invalid_too_short_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "061234",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_phone_invalid_wrong_prefix_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "1612345678",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_phone_invalid_wrong_country_code_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "+44612345678",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_phone_invalid_with_letters_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "2000-01-01",
              "countryOfResidence": "France",
              "phoneNumber": "06AB345678",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    // Tests about birthdate
    @Test
    void addUser_birthdate_exactly_18_returns_201() throws Exception {
        LocalDate birthdate = LocalDate.now().minusYears(18);
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "%s",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """.formatted(birthdate.toString());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.birthdate").value(birthdate.toString()));

        assertEquals(birthdate, userRepo.findAll().get(0).getBirthdate());
    }

    @Test
    void addUser_birthdate_over_18_returns_201() throws Exception {
        LocalDate birthdate = LocalDate.now().minusYears(30);
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "%s",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """.formatted(birthdate.toString());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.birthdate").value(birthdate.toString()));

        assertEquals(birthdate, userRepo.findAll().get(0).getBirthdate());
    }

    @Test
    void addUser_birthdate_null_returns_400() throws Exception {
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": null,
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Birthdate is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_birthdate_empty_returns_400() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "",
          "countryOfResidence": "France",
          "phoneNumber": "06 12 34 56 78",
          "gender": "Male"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Birthdate is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_birthdate_blank_returns_400() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "   ",
          "countryOfResidence": "France",
          "phoneNumber": "06 12 34 56 78",
          "gender": "Male"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Birthdate is required."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_birthdate_future_returns_400() throws Exception {
        LocalDate birthdate = LocalDate.now().plusYears(1);
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "%s",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """.formatted(birthdate.toString());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("Birthdate cannot be in the future."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_birthdate_under_18_returns_400() throws Exception {
        LocalDate birthdate = LocalDate.now().minusYears(1);
        String jsonRequest = """
            {
              "name": "Jean Valjean",
              "birthdate": "%s",
              "countryOfResidence": "France",
              "phoneNumber": "06 12 34 56 78",
              "gender": "Male"
            }
            """.formatted(birthdate.toString());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message").value("User must be at least 18 years old."));

        assertTrue(userRepo.findAll().isEmpty());
    }

    @Test
    void addUser_birthdate_wrong_format_returns_400_jsonParseError() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "12-05-1990",
          "countryOfResidence": "France",
          "phoneNumber": "06 12 34 56 78",
          "gender": "Male"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("JSON parse error"))
                .andExpect(jsonPath("$.message").value(
                        "JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"12-05-1990\": Failed to deserialize `java.time.LocalDate` (with format 'Value(Year,4,10,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)'): (java.time.format.DateTimeParseException) Text '12-05-1990' could not be parsed at index 0"
                ));

        assertTrue(userRepo.findAll().isEmpty());
    }

    // Tests about gender
    @Test
    void addUser_invalid_gender_returns_400_jsonParseError() throws Exception {
        String jsonRequest = """
        {
          "name": "Jean Valjean",
          "birthdate": "2000-01-01",
          "countryOfResidence": "France",
          "phoneNumber": "06 12 34 56 78",
          "gender": "Unknown"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("JSON parse error"))
                .andExpect(jsonPath("$.message").value("JSON parse error: Cannot deserialize value of type `com.boisseau.offertechnicaltest.model.Gender` from String \"Unknown\": not one of the values accepted for Enum class: [Other, Female, Male]"));

        assertTrue(userRepo.findAll().isEmpty());
    }
}
