package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.exception.BadRequestException;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.ClientInfo;
import org.morriswa.salon.model.EmployeeInfo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class ProfileDaoTest extends DaoTest {

    @Test
    public void getClientInfoQuery() throws Exception {
        var clientInfo = profileDao.getClientInfo(11L);

        assertEquals("client email is as expected",
                "client1@morriswa.org", clientInfo.getEmail());
        assertEquals("client pronouns are correct",
                "They/Them/Theirs", clientInfo.getPronouns());
    }

    @Test
    public void getClientInfoNoneExistQuery() {
        var exception = assertThrows(BadRequestException.class,
                ()->profileDao.getClientInfo(999_999_999L));

        assertNotNull("exception should be thrown", exception);
    }

    @Test
    public void updateClientInfoQuery() throws Exception {

        final var clientId = 11L;
        final var newFirstName = "My new first name";

        profileDao.updateClientInfo(clientId,
                new ClientInfo(
                newFirstName,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
                ));

        var info = profileDao.getClientInfo(clientId);

        assertEquals("database should be updated", newFirstName, info.getFirstName());
    }

    @Test
    public void updateClientInfoDuplicatePhoneQuery() {

        final var phoneNumber = "1111111131";
        final var clientId = 11L;

        var exception = assertThrows(ValidationException.class,
                ()->profileDao.updateClientInfo(clientId,
                        new ClientInfo(
                                null,
                                null,
                                null,
                                phoneNumber,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )));

        assertNotNull("exception should be thrown", exception);
        assertEquals("should include phone number in error field",
                "phoneNumber", exception.getValidationErrors().get(0).field());
        assertEquals("should include phone number in error field",
                phoneNumber, exception.getValidationErrors().get(0).rejectedValue());

    }

    @Test
    public void updateClientInfoDuplicateEmailQuery() {

        final var email = "user1@morriswa.org";
        final var clientId = 11L;

        var exception = assertThrows(ValidationException.class,
                ()->profileDao.updateClientInfo(clientId,
                        new ClientInfo(
                                null,
                                null,
                                null,
                                null,
                                email,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )));

        assertNotNull("exception should be thrown", exception);
        assertEquals("should include email in error field",
                "email", exception.getValidationErrors().get(0).field());
        assertEquals("should include email in error field",
                email, exception.getValidationErrors().get(0).rejectedValue());

    }

    @Test
    public void getEmployeeInfoQuery() throws Exception {
        var employeeInfo = profileDao.getEmployeeInfo(21L);

        assertEquals("employee email is as expected",
                "employee1@morriswa.org", employeeInfo.getEmail());
        assertEquals("employee pronouns are correct",
                "They/Them/Theirs", employeeInfo.getPronouns());
    }

    @Test
    public void getEmployeeInfoNoneExistQuery() {
        var exception = assertThrows(BadRequestException.class,
                ()->profileDao.getEmployeeInfo(11L));

        assertNotNull("exception should be thrown", exception);
    }

    @Test
    public void updateEmployeeInfoQuery() throws Exception {

        final var employeeId = 21L;
        final var newFirstName = "My new first name";

        profileDao.updateEmployeeProfile(employeeId,
                new EmployeeInfo(
                        newFirstName,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));

        var info = profileDao.getEmployeeInfo(employeeId);

        assertEquals("database should be updated", newFirstName, info.getFirstName());
    }

    @Test
    public void updateEmployeeInfoDuplicatePhoneQuery() {

        final var phoneNumber = "1111111131";
        final var employeeId = 21L;

        var exception = assertThrows(ValidationException.class,
                ()->profileDao.updateEmployeeProfile(employeeId,
                        new EmployeeInfo(
                                null,
                                null,
                                null,
                                phoneNumber,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )));

        assertNotNull("exception should be thrown", exception);
        assertEquals("should include phone number in error field",
                "phoneNumber", exception.getValidationErrors().get(0).field());
        assertEquals("should include phone number in error field",
                phoneNumber, exception.getValidationErrors().get(0).rejectedValue());

    }

    @Test
    public void updateEmployeeInfoDuplicateEmailQuery() {

        final var email = "user1@morriswa.org";
        final var employeeId = 21L;

        var exception = assertThrows(ValidationException.class,
                ()->profileDao.updateEmployeeProfile(employeeId,
                        new EmployeeInfo(
                                null,
                                null,
                                null,
                                null,
                                email,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                        )));

        assertNotNull("exception should be thrown", exception);
        assertEquals("should include email in error field",
                "email", exception.getValidationErrors().get(0).field());
        assertEquals("should include email in error field",
                email, exception.getValidationErrors().get(0).rejectedValue());

    }

}
