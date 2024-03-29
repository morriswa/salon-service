package org.morriswa.salon.dao;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.exception.ValidationException;
import org.morriswa.salon.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.*;

@Transactional
public class AccountDaoTest extends DaoTest {


    @Autowired protected PasswordEncoder encoder;

    @Test
    void registerAndFindUserQueries() throws Exception {

        final String username = "username";
        final String password = "password";

        dao.register(username, password);

        var createdUser = dao.findUser(username);

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
    void registerDuplicateUserQuery() throws Exception {

        final String username = "username";
        final String password = "password";

        dao.register(username, password);

        var exception = assertThrows(ValidationException.class, ()->dao.register(username, password));

        assertNotNull("should throw appropriate error", exception);
        assertTrue("exception should contain helpful info", exception.containsErrors());
        assertEquals("exception should report back an error in the 'username' field",
                "username", exception.getValidationErrors().get(0).field());
    }

    @Test
    void findUserBadNameQuery() {

        assertThrows(UsernameNotFoundException.class, ()->dao.findUser("bad_user"));
    }

    @Test
    void enterContactInfoQuery() throws Exception {
        final String username = "username";
        final String password = "password";

        dao.register(username, password);

        var createdUser = dao.findUser(username);

        assertNotNull("registered user should be present", createdUser);

        List<String> results = jdbcTemplate.query(
                "select * from user_account where username = :username",
                Map.of("username", username),
                rs -> {
                    var r = new ArrayList<String>();
                    while (rs.next()) {
                        r.add(rs.getString("username"));
                    }
                    return r;
                });

        assert results != null;
        assertEquals("database should have updated information",
                results.get(0), username);

        final Long userId = createdUser.getUserId();
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        dao.enterContactInfo(userId, newUserInfo);

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

        final String username = "username";
        final String password = "password";

        final String username1 = "username1";
        final String password1 = "password1";

        dao.register(username, password);
        dao.register(username1, password1);

        var createdUser = dao.findUser(username);
        var createdUser1 = dao.findUser(username1);

        assertNotNull("registered user should be present", createdUser);
        assertNotNull("registered user1 should be present", createdUser1);

        final Long userId = createdUser.getUserId();
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        final Long userId1 = createdUser1.getUserId();
        final UserInfo newUserInfo1 = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test1@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        assertNotEquals("users should have different ids", userId, userId1);

        dao.enterContactInfo(userId, newUserInfo);
        var exception = assertThrows(ValidationException.class,()->dao.enterContactInfo(userId1, newUserInfo1));

        assertEquals("exception should have helpful information 2",
                "phoneNumber", exception.getValidationErrors().get(0).field());
    }

    @Test
    void enterContactInfoDuplicateEmailQuery() throws Exception {

        final String username = "username";
        final String password = "password";

        final String username1 = "username1";
        final String password1 = "password1";

        dao.register(username, password);
        dao.register(username1, password1);

        var createdUser = dao.findUser(username);
        var createdUser1 = dao.findUser(username1);

        assertNotNull("registered user should be present", createdUser);
        assertNotNull("registered user1 should be present", createdUser1);

        final Long userId = createdUser.getUserId();
        final UserInfo newUserInfo = new UserInfo(
                "firstName", "lastName", "H",
                "1231231234", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        final Long userId1 = createdUser1.getUserId();
        final UserInfo newUserInfo1 = new UserInfo(
                "firstName", "lastName", "H",
                "1231231235", "test@morriswa.org",
                "1234 Main St", null, "Town", "KS", "12345",
                "Email"
        );

        assertNotEquals("users should have different ids", userId, userId1);

        dao.enterContactInfo(userId, newUserInfo);
        var exception = assertThrows(ValidationException.class,()->dao.enterContactInfo(userId1, newUserInfo1));

        assertEquals("exception should have helpful information",
                "email", exception.getValidationErrors().get(0).field());
    }
}
