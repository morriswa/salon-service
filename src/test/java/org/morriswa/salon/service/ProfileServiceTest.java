package org.morriswa.salon.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.morriswa.salon.annotations.WithClientAccount;
import org.morriswa.salon.annotations.WithEmployeeAccount;
import org.morriswa.salon.model.ClientInfo;
import org.morriswa.salon.model.EmployeeInfo;
import org.morriswa.salon.validation.UserProfileValidator;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("null")
public class ProfileServiceTest extends ServiceTest {

    @Test
    @WithClientAccount
    void updatePronouns() throws Exception {
        for (var pronoun : UserProfileValidator.validPronouns) {
            String request = String.format("""
            {
                "pronouns": "%s",
                "firstName": "testing"
            }
            """, pronoun);

            hit(HttpMethod.PATCH, "/client/profile", request)
                    .andExpect(status().is(204))
            ;
        }

        verify(profileDao, times(UserProfileValidator.validPronouns.size()))
            .updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyPronouns() throws Exception {
        String request = """
        {
            "pronouns": " ",
            "firstName": "testing"
        }
        """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("pronouns")))
        ;

        verify(profileDao, never())
                .updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectInvalidPronouns() throws Exception {
        for (var pronoun : Set.of("A", "B", "C")) {
            String request = String.format("""
            {
                "pronouns": "%s",
                "firstName": "testing"
            }
            """, pronoun);

            hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("pronouns")))
            ;
        }

        verify(profileDao, never())
                .updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updateFirstName() throws Exception {
        String request = """
            {
                "firstName": "testing"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyFirstName() throws Exception {
        String request = """
            {
                "firstName": " ",
                "lastName": "test"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("firstName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void rejectLongFirstName() throws Exception {

        final var longName = "long".repeat(8) + "n";
        assert longName.length() == 33;

        String request = String.format("""
            {
                "firstName": "%s",
                "lastName": "test"
            }
            """, longName);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("firstName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void updateLastName() throws Exception {
        String request = """
            {
                "lastName": "testing"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyLastName() throws Exception {
        String request = """
            {
                "firstName": "test",
                "lastName": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("lastName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void rejectLongLastName() throws Exception {

        final var longName = "long".repeat(8) + "n";
        assert longName.length() == 33;

        String request = String.format("""
            {
                "firstName": "name",
                "lastName": "%s"
            }
            """, longName);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("lastName")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void updateContactPreference() throws Exception {

        for (var pref : List.of("PhoneCall", "TextMessage", "Email")) {

            String request = String.format("""
                    {
                        "lastName": "test",
                        "contactPreference": "%s"
                    }
                    """, pref);

            hit(HttpMethod.PATCH, "/client/profile", request)
                    .andExpect(status().is(204))
            ;

        }

        verify(profileDao, times(3)).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectInvalidContactPreference() throws Exception {

        for (var pref : List.of("Phonecall", "textMessage", "email")) {

            String request = String.format("""
                    {
                        "lastName": "test",
                        "contactPreference": "%s"
                    }
                    """, pref);

            hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
                    .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
            ;
        }

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updatePhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1112223334"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
            .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectLongPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "11234567890"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
            .andExpect(status().is(400))
            .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectInvalidPhoneNumber() throws Exception {
        String request = """
            {
                "phoneNumber": "+1 (913) 777"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectInvalidPhoneNumber2() throws Exception {
        String request = """
            {
                "phoneNumber": "1 34567890"
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }

    @Test
    @WithClientAccount
    void rejectEmptyNumber() throws Exception {
        String request = """
            {
                "phoneNumber": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updateAddress() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void updateAddressEmptyLineTwo() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": " ",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyAddressLineOne() throws Exception {
        String request = """
            {
                "addressLineOne": " ",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("addressLineOne")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongAddress() throws Exception {

        final var longAddress = "address".repeat(7) + "hi";
        assert longAddress.length() == 51;

        String request = String.format("""
            {
                "addressLineOne": "%s",
                "addressLineTwo": "%s",
                "city": "Georgetown",
                "state": "DC"
            }
            """, longAddress, longAddress);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("addressLineOne")))
                .andExpect(jsonPath("$.additionalInfo[1].field", Matchers.is("addressLineTwo")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyCity() throws Exception {

        final var longCity = "address".repeat(7) + "hi";
        assert longCity.length() == 51;

        String request = String.format("""
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "%s",
                "state": "DC"
            }
            """, longCity);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("city")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongCity() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": " ",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("city")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "K"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectInvalidState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "K "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongState() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "stateCode": "KSS"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("stateCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }


    @Test
    @WithClientAccount
    void updateZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "12345"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectShortZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "1234"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectLongZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": "123456"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyZip() throws Exception {
        String request = """
            {
                "lastName": "test",
                "zipCode": " "
            }
            """;

        ClientInfo info = mapper.readValue(request, ClientInfo.class);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("zipCode")))
        ;

        verify(profileDao, never()).updateClientInfo(testingUserId, info);
    }


    @Test
    @WithClientAccount
    void getClientProfile() throws Exception {

        when(profileDao.getClientInfo(testingUserId))
            .thenReturn(new ClientInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", null, "City", "ST", "12345",
                "Email", null));

        hit(HttpMethod.GET, "/client/profile")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithClientAccount
    void getUserProfileIncludingAddressLineTwo() throws Exception {

        when(profileDao.getClientInfo(testingUserId))
            .thenReturn(new ClientInfo("First", "Last", "He/Him/His",
                "1234567890", "test@email.com", 
                "1234 Test Ave.", "Apt 567", "City", "ST", "12345",
                "Email", null));

        hit(HttpMethod.GET, "/client/profile")
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
            .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithClientAccount
    void updateEmail() throws Exception {
        String request = """
            {
                "firstName": "test@morriswa.org"
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateClientInfo(eq(testingUserId), any(ClientInfo.class));
    }

    @Test
    @WithClientAccount
    void rejectEmptyEmail() throws Exception {
        String request = """
            {
                "firstName": "test",
                "lastName": "test",
                "email": " "
            }
            """;

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("email")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }

    @Test
    @WithClientAccount
    void rejectLongEmail() throws Exception {

        final var longEmail = "email".repeat(10) + "@" + "email".repeat(10);
        assert longEmail.length() == 101;

        String request = String.format("""
            {
                "firstName": "test",
                "lastName": "test",
                "email": "%s"
            }
            """, longEmail);

        hit(HttpMethod.PATCH, "/client/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("email")))
        ;

        verify(profileDao, never()).updateClientInfo(any(), any());
    }


    @Test
    @WithEmployeeAccount
    void updatePronounEmployeeAccount() throws Exception {
        for (var pronoun : UserProfileValidator.validPronouns) {
            String request = String.format("""
            {
                "pronouns": "%s",
                "firstName": "testing"
            }
            """, pronoun);

            hit(HttpMethod.PATCH, "/employee/profile", request)
                    .andExpect(status().is(204))
            ;
        }

        verify(profileDao, times(UserProfileValidator.validPronouns.size()))
                .updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectEmptyPronounsEmployeeAccount() throws Exception {
        String request = """
        {
            "pronouns": " ",
            "firstName": "testing"
        }
        """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("pronouns")))
        ;

        verify(profileDao, never())
                .updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectInvalidPronounsEmployeeAccount() throws Exception {
        for (var pronoun : Set.of("A", "B", "C")) {
            String request = String.format("""
            {
                "pronouns": "%s",
                "firstName": "testing"
            }
            """, pronoun);

            hit(HttpMethod.PATCH, "/employee/profile", request)
                    .andExpect(status().is(400))
                    .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("pronouns")))
            ;
        }

        verify(profileDao, never())
                .updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void updatePhoneNumberEmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": "1112223334"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectShortPhoneNumberEmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": "1234567"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectLongPhoneNumberEmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": "11234567890"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectInvalidPhoneNumberEmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": "+1 (913) 777"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectInvalidPhoneNumber2EmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": "1 34567890"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectEmptyNumberEmployeeAccount() throws Exception {
        String request = """
            {
                "phoneNumber": " "
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers.is("ValidationException")))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("phoneNumber")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void updateAddressEmployeeAccount() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": "Apt 123",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void updateAddressEmptyLineTwoEmployeeAccount() throws Exception {
        String request = """
            {
                "addressLineOne": "1234 Main St.",
                "addressLineTwo": " ",
                "city": "Georgetown",
                "state": "DC"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void getEmployeeProfile() throws Exception {

        when(profileDao.getEmployeeInfo(testingUserId))
                .thenReturn(new EmployeeInfo("First", "Last", "He/Him/His",
                        "1234567890", "test@email.com",
                        "1234 Test Ave.", null, "City", "ST", "12345",
                        "Email", null));

        hit(HttpMethod.GET, "/employee/profile")
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithEmployeeAccount
    void getPublicEmployeeProfile() throws Exception {

        when(profileDao.getEmployeeInfo(testingUserId))
                .thenReturn(new EmployeeInfo("First", "Last", "He/Him/His",
                        "1234567890", "test@email.com",
                        "1234 Test Ave.", null, "City", "ST", "12345",
                        "Email", null));

        hit(HttpMethod.GET, String.format("/public/profile/%d", testingUserId))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email", Matchers.is("test@email.com")))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithEmployeeAccount
    void getFeaturedEmployeeProfiles() throws Exception {

        final Long featuredTestOne = 11L;
        final Long featuredTestTwo = 22L;
        final Long featuredTestThree = 33L;


        when(profileDao.getEmployeeInfo(featuredTestOne))
                .thenReturn(new EmployeeInfo("First", "Last", "He/Him/His",
                        "1234567890", "test1@email.com",
                        "1234 Test Ave.", null, "City", "ST", "12345",
                        "Email", null));

        when(profileDao.getEmployeeInfo(featuredTestTwo))
                .thenReturn(new EmployeeInfo("Second", "Last", "He/Him/His",
                        "2234567890", "test2@email.com",
                        "1234 Test Ave.", null, "City", "ST", "12345",
                        "Email", null));

        when(profileDao.getEmployeeInfo(featuredTestThree))
                .thenReturn(new EmployeeInfo("Third", "Last", "He/Him/His",
                        "3234567890", "test3@email.com",
                        "1234 Test Ave.", null, "City", "ST", "12345",
                        "Email", null));

        hit(HttpMethod.GET, "/public/featuredEmployees")
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].employeeId", Matchers.is(featuredTestOne.intValue())))
                .andExpect(jsonPath("$[1].employeeId", Matchers.is(featuredTestTwo.intValue())))
                .andExpect(jsonPath("$[2].employeeId", Matchers.is(featuredTestThree.intValue())))
        ;
    }

    @Test
    @WithEmployeeAccount
    void getEmployeeProfileIncludingAddressLineTwo() throws Exception {

        when(profileDao.getEmployeeInfo(testingUserId))
                .thenReturn(new EmployeeInfo("First", "Last", "He/Him/His",
                        "1234567890", "test@email.com",
                        "1234 Test Ave.", "Apt 567", "City", "ST", "12345",
                        "Email", null));

        hit(HttpMethod.GET, "/employee/profile")
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.addressLineOne", Matchers.is("1234 Test Ave.")))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is("1234567890")))
        ;
    }

    @Test
    @WithEmployeeAccount
    void updateEmailEmployeeAccount() throws Exception {
        String request = """
            {
                "firstName": "test@morriswa.org"
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(204))
        ;

        verify(profileDao).updateEmployeeProfile(eq(testingUserId), any(EmployeeInfo.class));
    }

    @Test
    @WithEmployeeAccount
    void rejectEmptyEmailWithEmployeeAccount() throws Exception {
        String request = """
            {
                "firstName": "test",
                "lastName": "test",
                "email": " "
            }
            """;

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("email")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(any(), any());
    }

    @Test
    @WithEmployeeAccount
    void rejectLongEmailWithEmployeeAccount() throws Exception {

        final var longEmail = "email".repeat(10) + "@" + "email".repeat(10);
        assert longEmail.length() == 101;

        String request = String.format("""
            {
                "firstName": "test",
                "lastName": "test",
                "email": "%s"
            }
            """, longEmail);

        hit(HttpMethod.PATCH, "/employee/profile", request)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.additionalInfo[0].field", Matchers.is("email")))
        ;

        verify(profileDao, never()).updateEmployeeProfile(any(), any());
    }
}
