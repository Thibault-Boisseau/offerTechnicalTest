package com.boisseau.offertechnicaltest.service;

import com.boisseau.offertechnicaltest.model.Gender;
import com.boisseau.offertechnicaltest.model.User;
import com.boisseau.offertechnicaltest.model.dto.UserCreationRequest;
import com.boisseau.offertechnicaltest.model.dto.UserResponse;
import com.boisseau.offertechnicaltest.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserRegisterService userRegisterService;

    // verifyUserName tests
    @Test
    void verifyUserName_valid() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserName("Jean Valjean"));
    }

    @Test
    void verifyUserName_invalid_null_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserName(null));
        assertEquals("Name is required.", ex.getMessage());
    }

    @Test
    void verifyUserName_invalid_empty_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserName(""));
        assertEquals("Name is required.", ex.getMessage());
    }

    @Test
    void verifyUserName_invalid_blank_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserName(" "));
        assertEquals("Name is required.", ex.getMessage());
    }

    // verifyUserCountryOfResidence tests
    @Test
    void verifyUserCountry_france_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserCountryOfResidence("France"));
    }

    @Test
    void verifyUserCountry_france_mixed_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserCountryOfResidence("FrAnCe"));
    }

    @Test
    void verifyUserCountry_other_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserCountryOfResidence("Belgium"));
        assertEquals("Only users from France are allowed.", ex.getMessage());
    }

    @Test
    void verifyUserCountry_null_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserCountryOfResidence(null));
        assertEquals("Country of residence is required.", ex.getMessage());
    }

    @Test
    void verifyUserCountry_empty_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserCountryOfResidence(""));
        assertEquals("Country of residence is required.", ex.getMessage());
    }

    @Test
    void verifyUserCountry_blank_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserCountryOfResidence("   "));
        assertEquals("Country of residence is required.", ex.getMessage());
    }

    // verifyUserPhoneNumber tests
    @Test
    void verifyUserPhoneNumber_valid_national_compact_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserPhoneNumber("0612345678"));
    }

    @Test
    void verifyUserPhoneNumber_valid_national_with_spaces_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserPhoneNumber("06 12 34 56 78"));
    }

    @Test
    void verifyUserPhoneNumber_valid_international_compact_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserPhoneNumber("+33612345678"));
    }

    @Test
    void verifyUserPhoneNumber_valid_international_with_spaces_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserPhoneNumber("+33 6 12 34 56 78"));
    }

    @Test
    void verifyUserPhoneNumber_null_ok() {
        assertDoesNotThrow(() -> userRegisterService.verifyUserPhoneNumber(null));
    }

    @Test
    void verifyUserPhoneNumber_empty_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber(""));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    @Test
    void verifyUserPhoneNumber_blank_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber("       "));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    @Test
    void verifyUserPhoneNumber_invalid_too_short_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber("061234"));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    @Test
    void verifyUserPhoneNumber_invalid_wrong_prefix_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber("1612345678"));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    @Test
    void verifyUserPhoneNumber_invalid_wrong_country_code_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber("+44612345678"));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    @Test
    void verifyUserPhoneNumber_invalid_with_letters_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserPhoneNumber("06AB345678"));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));
    }

    // verifyUserBirthdate tests
    @Test
    void verifyUserBirthdate_exactly_18_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(18);
        assertDoesNotThrow(() -> userRegisterService.verifyUserBirthdate(birthdate));
    }

    @Test
    void verifyUserBirthdate_over_18_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(30);
        assertDoesNotThrow(() -> userRegisterService.verifyUserBirthdate(birthdate));
    }

    @Test
    void verifyUserBirthdate_null_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserBirthdate(null));
        assertEquals("Birthdate is required.", ex.getMessage());
    }

    @Test
    void verifyUserBirthdate_future_throws() {
        LocalDate birthdate = LocalDate.now().plusYears(1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserBirthdate(birthdate));
        assertEquals("Birthdate cannot be in the future.", ex.getMessage());
    }

    @Test
    void verifyUserBirthdate_under_18_throws() {
        LocalDate birthdate = LocalDate.now().minusYears(1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.verifyUserBirthdate(birthdate));
        assertEquals("User must be at least 18 years old.", ex.getMessage());
    }

    // addUser tests

    // about username
    @Test
    void addUser_valid_name_saves_user() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));
        verify(userRepo, times(1)).save(any(User.class));

        assertEquals("Jean Valjean", response.name());
        assertEquals(1, response.id());
    }

    @Test
    void addUser_invalid_name_null_throws_and_does_no_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                null,
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Name is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_invalid_Name_empty_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Name is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_invalid_name_blank_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                " ",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Name is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

// about country of residence
    @Test
    void addUser_country_France_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("France", response.countryOfResidence());
    }

    @Test
    void addUser_country_France_mixed_case_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "FrAnCe",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("FrAnCe", response.countryOfResidence());
    }

    @Test
    void addUser_country_other_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "Belgium",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Only users from France are allowed.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_country_null_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                null,
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Country of residence is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_country_empty_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Country of residence is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_country_blank_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "   ",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Country of residence is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }


// about phone number
    @Test
    void addUser_phone_valid_national_compact_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "0612345678",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("0612345678", response.phoneNumber());
    }

    @Test
    void addUser_phone_valid_national_with_spaces_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("06 12 34 56 78", response.phoneNumber());
    }

    @Test
    void addUser_phone_valid_international_compact_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "+33612345678",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("+33612345678", response.phoneNumber());
    }

    @Test
    void addUser_phone_valid_international_with_spaces_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "+33 6 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("+33 6 12 34 56 78", response.phoneNumber());
    }

    @Test
    void addUser_phone_empty_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_phone_blank_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "   ",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_phone_invalid_too_short_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "061234",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_phone_invalid_wrongPrefix_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "1612345678",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_phone_invalid_wrong_country_code_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "+44612345678",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_phone_invalid_with_letters_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(25);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06AB345678",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertTrue(ex.getMessage().startsWith("Your phone number is missing or invalid. It must be in the format 0X XX XX XX XX or +33 X XX XX XX XX."));

        verify(userRepo, never()).save(any());
    }


// about birthdate
    @Test
    void addUser_birthdate_exactly_18_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(18);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals(birthdate, response.birthdate());
    }

    @Test
    void addUser_birthdate_over_18_ok() {
        LocalDate birthdate = LocalDate.now().minusYears(30);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        User saved = new User();
        saved.setId(1);
        saved.setName(request.name());
        saved.setBirthdate(request.birthdate());
        saved.setCountryOfResidence(request.countryOfResidence());
        saved.setPhoneNumber(request.phoneNumber());
        saved.setGender(request.gender());

        when(userRepo.save(any(User.class))).thenReturn(saved);

        UserResponse response = assertDoesNotThrow(() -> userRegisterService.addUser(request));

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals(birthdate, response.birthdate());
    }

    @Test
    void addUser_birthdate_null_throws_and_does_not_save() {
        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                null,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Birthdate is required.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_birthdate_future_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().plusYears(1);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("Birthdate cannot be in the future.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }

    @Test
    void addUser_birthdate_under_18_throws_and_does_not_save() {
        LocalDate birthdate = LocalDate.now().minusYears(1);

        UserCreationRequest request = new UserCreationRequest(
                "Jean Valjean",
                birthdate,
                "France",
                "06 12 34 56 78",
                Gender.MALE
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userRegisterService.addUser(request));
        assertEquals("User must be at least 18 years old.", ex.getMessage());

        verify(userRepo, never()).save(any());
    }
}
