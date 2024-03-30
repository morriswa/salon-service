package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.*;

public class AccountDaoTest extends DaoTest {


    @Autowired protected PasswordEncoder encoder;

    @Test
    void registerAndFindUserQueries() throws Exception {

        final String username = "new_user";
        final String password = "password";

        accountDao.register(username, password);

        var createdUser = accountDao.findUser(username);

        assertNotNull("registered user should be present", createdUser);
        assertNotNull("new user must have user id", createdUser.getUserId());
        assertNotNull("new user must have account creation date", createdUser.getDateCreated());
        assertEquals("registered user should have requested username", username, createdUser.getUsername());
        assertTrue("registered user should have requested password", encoder.matches(password, createdUser.getPassword()));
        assertTrue(
                "registered user should have correct authorities",
                createdUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );
    }

    @Test
    void registerDuplicateUserQuery() {

        final String username = "test_nuser_1";
        final String password = "password";

        var exception = assertThrows(ValidationException.class, ()-> accountDao.register(username, password));

        assertNotNull("should throw appropriate error", exception);
        assertTrue("exception should contain helpful info", exception.containsErrors());
        assertEquals("exception should report back an error in the 'username' field",
                "username", exception.getValidationErrors().get(0).field());
    }

    @Test
    void findUserClientQuery() {

        var clientUser = accountDao.findUser("test_client_1");

        assertNotNull("client user should already be in db", clientUser);

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );
    }

    @Test
    void findUserEmployeeQuery() {

        var clientUser = accountDao.findUser("test_employee_1");

        assertNotNull("employee user should already be in db", clientUser);

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );
    }

    @Test
    void findUserBadNameQuery() {

        assertThrows(UsernameNotFoundException.class, ()-> accountDao.findUser("bad_user"));
    }

    @Test
    void enterContactInfoQuery() throws Exception {

        final Long userId = 1L;
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        accountDao.enterContactInfo(userId, newUserInfo);

        List<String> contactInfoResults = jdbcTemplate.query(
                "select * from contact_info where user_id = :userId",
                Map.of("userId", userId),
                rs -> {
                    var r = new ArrayList<String>();
                    while (rs.next()) {
                        r.add(rs.getString("email"));
                    }
                    return r;
                });

        assert contactInfoResults != null;
        assertEquals("database should have updated contact information",
                contactInfoResults.get(0), newUserInfo.getEmail());

    }

    @Test
    void enterContactInfoDuplicatePhoneQuery() throws Exception {

        final Long userId = 1L;
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        final Long userId1 = 2L;
        final UserInfo newUserInfo1 = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test1@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        accountDao.enterContactInfo(userId, newUserInfo);
        var exception = assertThrows(ValidationException.class,()-> accountDao.enterContactInfo(userId1, newUserInfo1));

        assertEquals("exception should have helpful information 2",
                "phoneNumber", exception.getValidationErrors().get(0).field());
    }

    @Test
    void enterContactInfoDuplicateEmailQuery() throws Exception {

        final Long userId = 5L;
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        final Long userId1 = 1L;
        final UserInfo newUserInfo1 = new UserInfo(
                "firstName", "lastName", "H",
                "1231231235", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        accountDao.enterContactInfo(userId, newUserInfo);
        var exception = assertThrows(ValidationException.class,()-> accountDao.enterContactInfo(userId1, newUserInfo1));

        assertEquals("exception should have helpful information",
                "email", exception.getValidationErrors().get(0).field());
    }

    @Test
    void completeClientRegistrationQuery() {

        var user = accountDao.findUser("test_user_1");

        assertNotNull("user should already be in db", user);

        assertTrue(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertFalse(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertFalse(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );

        accountDao.completeClientRegistration(user.getUserId());

        var clientUser = accountDao.findUser("test_user_1");

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );

    }

    @Test
    void completeEmployeeRegistrationQuery() {

        var user = accountDao.findUser("test_user_1");

        assertNotNull("user should already be in db", user);

        assertTrue(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertFalse(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertFalse(
                "registered user should have correct authorities",
                user.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );

        accountDao.completeEmployeeRegistration(user.getUserId());

        var clientUser = accountDao.findUser("test_user_1");

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("NUSER"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("USER"))
        );

        assertFalse(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))
        );

        assertTrue(
                "registered user should have correct authorities",
                clientUser.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))
        );

    }
}
